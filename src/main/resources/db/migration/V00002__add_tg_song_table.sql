DROP TABLE IF EXISTS relationship_song_folder;
DROP TABLE IF EXISTS tg_song;

-- Create tg_song table
CREATE TABLE tg_song (
    title VARCHAR(100),
    download_id INTEGER NOT NULL UNIQUE PRIMARY KEY,
    file_id VARCHAR UNIQUE,
    artist VARCHAR,
    duration VARCHAR);

