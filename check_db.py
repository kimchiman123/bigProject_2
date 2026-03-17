
import os
import psycopg2
import urllib.parse

def parse_db_url(url):
    if not url: return {}
    info = {}
    try:
        if url.startswith("jdbc:"): url = url[5:]
        parsed = urllib.parse.urlparse(url)
        if parsed.scheme in ('postgresql', 'postgres'):
            info['host'] = parsed.hostname
            info['port'] = parsed.port
            info['dbname'] = parsed.path.lstrip('/')
            info['user'] = parsed.username
            info['password'] = parsed.password
    except Exception as e:
        print(f"URL parsing failed: {e}")
    return info

SPRING_URL = os.environ.get("SPRING_DATASOURCE_URL", "jdbc:postgresql://localhost:5432/bigproject")
parsed_info = parse_db_url(SPRING_URL)

DB_HOST = os.environ.get("DB_HOST", "localhost")
DB_PORT = os.environ.get("DB_PORT", "5432")
DB_NAME = os.environ.get("POSTGRES_DB", "bigproject")
DB_USER = os.environ.get("POSTGRES_USER", "postgres")
DB_PASS = os.environ.get("POSTGRES_PASSWORD", "postgres")

try:
    conn = psycopg2.connect(
        host=DB_HOST, dbname=DB_NAME, user=DB_USER, password=DB_PASS, port=DB_PORT,
        sslmode="disable"
    )
    cur = conn.cursor()
    cur.execute("SELECT count(*) FROM export_trends")
    print(f"export_trends count: {cur.fetchone()[0]}")
    cur.execute("SELECT count(*) FROM amazon_reviews")
    print(f"amazon_reviews count: {cur.fetchone()[0]}")
    conn.close()
except Exception as e:
    print(f"Error: {e}")
