import pytest
from main import is_generic_term, calculate_relevance_score

def test_is_generic_term():
    # 1. 단일 불용어인 경우 True 반환 검증 (GENERIC_INSIGHT_STOPWORDS)
    assert is_generic_term("good") is True
    assert is_generic_term("recipe") is True
    
    # 2. 감각 불용어인 경우 True 반환 검증 (SENSORY_STOPWORDS)
    assert is_generic_term("delicious") is True
    assert is_generic_term("taste") is True
    
    # 3. 불용어가 조합된 Bigram일 때 True 반환 검증
    assert is_generic_term("really good") is True
    assert is_generic_term("delicious food") is True
    
    # 4. 불용어가 아닌 핵심 단어가 포함되었을 때 False 반환 검증
    assert is_generic_term("spicy ramen") is False
    assert is_generic_term("crispy chicken") is False


def test_calculate_relevance_score():
    # 1. 식감 키워드(spicy) 가중치 적용 검증 (기본 1.8배 가중치)
    score_spicy = calculate_relevance_score("spicy sauce", mention_count=10, impact_score=0.5)
    score_normal = calculate_relevance_score("normal sauce", mention_count=10, impact_score=0.5)
    
    assert score_spicy > score_normal
    
    # 2. 페어링 키워드(rice) 가중치 적용 검증 (기본 1.4배 가중치)
    score_pairing = calculate_relevance_score("with rice", mention_count=10, impact_score=0.5)
    score_normal2 = calculate_relevance_score("normal test", mention_count=10, impact_score=0.5)
    
    assert score_pairing > score_normal2

    # 3. 범용성 단어 패널티 적용 검증 (식감/페어링이 없는 범용 단어 0.3배 페널티)
    score_generic = calculate_relevance_score("really good", mention_count=10, impact_score=0.5)
    assert score_generic < score_normal2
