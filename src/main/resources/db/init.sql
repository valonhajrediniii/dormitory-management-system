CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'USER')),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS student_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    faculty VARCHAR(120) NOT NULL,
    year_of_study INT NOT NULL CHECK (year_of_study BETWEEN 1 AND 6),
    gender VARCHAR(20),
    phone VARCHAR(30),
    city VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS dormitories (
    id BIGSERIAL PRIMARY KEY,
    dorm_number VARCHAR(20) NOT NULL UNIQUE,
    dorm_name VARCHAR(120) NOT NULL,
    location VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS rooms (
    id BIGSERIAL PRIMARY KEY,
    dormitory_id BIGINT NOT NULL REFERENCES dormitories(id) ON DELETE CASCADE,
    room_number VARCHAR(20) NOT NULL,
    capacity INT NOT NULL CHECK (capacity > 0),
    occupied_beds INT NOT NULL DEFAULT 0 CHECK (occupied_beds >= 0),
    status VARCHAR(20) NOT NULL CHECK (status IN ('FREE', 'PARTIAL', 'FULL')),
    UNIQUE (dormitory_id, room_number),
    CHECK (occupied_beds <= capacity)
);

CREATE TABLE IF NOT EXISTS applications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    application_date TIMESTAMP NOT NULL DEFAULT NOW(),
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED')),
    dormitory_id BIGINT REFERENCES dormitories(id) ON DELETE SET NULL,
    room_id BIGINT REFERENCES rooms(id) ON DELETE SET NULL,
    notes TEXT
);

CREATE TABLE IF NOT EXISTS tickets (
    id BIGSERIAL PRIMARY KEY,
    application_id BIGINT NOT NULL UNIQUE REFERENCES applications(id) ON DELETE CASCADE,
    qr_data TEXT NOT NULL,
    issued_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS complaints (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    subject VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'OPEN' CHECK (status IN ('OPEN', 'ANSWERED', 'CLOSED')),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS complaint_replies (
    id BIGSERIAL PRIMARY KEY,
    complaint_id BIGINT NOT NULL REFERENCES complaints(id) ON DELETE CASCADE,
    admin_id BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    reply_message TEXT NOT NULL,
    replied_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_applications_status ON applications(status);
CREATE INDEX IF NOT EXISTS idx_rooms_status ON rooms(status);
CREATE INDEX IF NOT EXISTS idx_complaints_status ON complaints(status);
CREATE INDEX IF NOT EXISTS idx_complaint_replies_complaint_id ON complaint_replies(complaint_id);

INSERT INTO users (full_name, email, password, role)
VALUES ('System Admin', 'admin@dormitory.local', 'admin123', 'ADMIN')
ON CONFLICT (email) DO NOTHING;
