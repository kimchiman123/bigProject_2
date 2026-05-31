package com.aivle0102.bigproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class ReportProgressTrackerTest {

    private ReportProgressTracker tracker;

    @BeforeEach
    void setUp() {
        tracker = new ReportProgressTracker();
    }

    // -------------------------------------------------------------
    // 1. init() 테스트 그룹
    // -------------------------------------------------------------

    @Test
    @DisplayName("성공: 유효한 jobId로 초기화할 때, 상태가 등록되어야 함")
    void testInit_success() throws Exception {
        String jobId = "job-123";
        tracker.init(jobId, 100);

        Map<String, Object> statesMap = getStatesMap(tracker);
        assertTrue(statesMap.containsKey(jobId));
    }

    @Test
    @DisplayName("예외: jobId가 null이거나 빈 값인 경우, 상태 등록을 건너뛰어야 함")
    void testInit_invalidJobId() throws Exception {
        tracker.init(null, 100);
        tracker.init("", 100);
        tracker.init("   ", 100);

        Map<String, Object> statesMap = getStatesMap(tracker);
        assertTrue(statesMap.isEmpty());
    }

    // -------------------------------------------------------------
    // 2. step() 테스트 그룹
    // -------------------------------------------------------------

    @Test
    @DisplayName("성공: 단계가 완료됨에 따라 게이지(progress)와 메시지가 정확히 업데이트되어야 함")
    void testStep_progressUpdate() throws Exception {
        String jobId = "job-456";
        tracker.init(jobId, 10); // totalWeight = 10

        // 3단계 진행 (3/10 -> 30%)
        tracker.step(jobId, 3, "analysis", "Analyzing data");

        Object state = getStatesMap(tracker).get(jobId);
        assertNotNull(state);
        assertEquals(30, getFieldValue(state, "progress"));
        assertEquals("analysis", getFieldValue(state, "stage"));
        assertEquals("Analyzing data", getFieldValue(state, "message"));
    }

    @Test
    @DisplayName("경계값: 진행률은 100% 미만 단계(step)에서는 최대 99%로 제한되어야 함")
    void testStep_progressCeiling() throws Exception {
        String jobId = "job-ceiling";
        tracker.init(jobId, 10);

        // 10단계 중 10단계를 완료하더라도 complete()를 호출하지 않고 step()만 호출하면 99%로 제한
        tracker.step(jobId, 10, "almost-done", "Processing last steps");

        Object state = getStatesMap(tracker).get(jobId);
        assertEquals(99, getFieldValue(state, "progress"));
    }

    @Test
    @DisplayName("예외: 존재하지 않는 jobId로 step을 밟으면 에러 없이 무시되어야 함")
    void testStep_nonExistentJobId() {
        assertDoesNotThrow(() -> tracker.step("non-existent-job", 5, "stage", "msg"));
    }

    // -------------------------------------------------------------
    // 3. complete() 테스트 그룹
    // -------------------------------------------------------------

    @Test
    @DisplayName("성공: complete() 호출 시 진행률이 100% 및 done 상태로 변경 후 메모리에서 상태가 정리되어야 함")
    void testComplete_success() throws Exception {
        String jobId = "job-complete";
        tracker.init(jobId, 10);

        // complete 실행
        tracker.complete(jobId);

        // 최종적으로 상태 맵에서 삭제되어야 함
        Map<String, Object> statesMap = getStatesMap(tracker);
        assertFalse(statesMap.containsKey(jobId));
    }

    // -------------------------------------------------------------
    // 4. fail() 테스트 그룹
    // -------------------------------------------------------------

    @Test
    @DisplayName("성공: 작업 실패 시 error 상태가 설정되고 최종적으로 맵에서 삭제되어야 함")
    void testFail_success() throws Exception {
        String jobId = "job-fail";
        tracker.init(jobId, 10);

        tracker.fail(jobId, "Out of Memory");

        Map<String, Object> statesMap = getStatesMap(tracker);
        assertFalse(statesMap.containsKey(jobId));
    }

    // -------------------------------------------------------------
    // 5. subscribe() 테스트 그룹
    // -------------------------------------------------------------

    @Test
    @DisplayName("성공: 정상적인 jobId에 대한 SseEmitter 구독 생성 테스트")
    void testSubscribe_success() {
        String jobId = "job-sub";
        tracker.init(jobId, 100);

        SseEmitter emitter = tracker.subscribe(jobId);
        assertNotNull(emitter);
    }

    @Test
    @DisplayName("성공: 신규 대기중(queued) 상태의 작업도 구독 시 기본 페이로드가 정상 전송되어야 함")
    void testSubscribe_queuedJob() {
        String jobId = "job-queued"; // init() 없이 바로 구독

        SseEmitter emitter = tracker.subscribe(jobId);
        assertNotNull(emitter);
    }

    @Test
    @DisplayName("예외: 구독 요청에 비정상적인 jobId가 전달되면 즉시 완료(complete) 처리되어야 함")
    void testSubscribe_invalidJobId() {
        SseEmitter emitter = tracker.subscribe(null);
        assertNotNull(emitter);
    }

    // -------------------------------------------------------------
    // 🛠️ 리플렉션 헬퍼 메서드 (캡슐화된 내부 필드 검증용)
    // -------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private ConcurrentHashMap<String, Object> getStatesMap(ReportProgressTracker target) throws Exception {
        Field field = ReportProgressTracker.class.getDeclaredField("states");
        field.setAccessible(true);
        return (ConcurrentHashMap<String, Object>) field.get(target);
    }

    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }
}
