-- 1. Insert Roles
INSERT INTO roles (id, role_name) VALUES 
(1, 'ADMIN'),
(2, 'MENTOR'),
(3, 'STUDENT')
ON CONFLICT (id) DO NOTHING;

-- 2. Insert Users (Mật khẩu mặc định của tất cả tài khoản: password123)
-- BCrypt hash của 'password123': $2a$10$p3.20U9oN7q1l/bXh8Z3Vuk84l1YdC1.Z0Y2fG0wW7R1e/pCg9/bS
INSERT INTO users (user_id, username, password_hash, full_name, email, phone_number, role_id, is_active, created_at, updated_at) VALUES
(1, 'admin', '$2a$10$p3.20U9oN7q1l/bXh8Z3Vuk84l1YdC1.Z0Y2fG0wW7R1e/pCg9/bS', 'Hệ Thống Administrator', 'admin@example.com', '0123456789', 1, true, NOW(), NOW()),
(2, 'mentor1', '$2a$10$p3.20U9oN7q1l/bXh8Z3Vuk84l1YdC1.Z0Y2fG0wW7R1e/pCg9/bS', 'Nguyễn Văn Mentor', 'mentor1@example.com', '0987654321', 2, true, NOW(), NOW()),
(5, 'mentor2', '$2a$10$p3.20U9oN7q1l/bXh8Z3Vuk84l1YdC1.Z0Y2fG0wW7R1e/pCg9/bS', 'Trần Thị Mentor', 'mentor2@example.com', '0987654322', 2, true, NOW(), NOW()),
(3, 'student1', '$2a$10$p3.20U9oN7q1l/bXh8Z3Vuk84l1YdC1.Z0Y2fG0wW7R1e/pCg9/bS', 'Trần Văn Student', 'student1@example.com', '0909090909', 3, true, NOW(), NOW()),
(4, 'student2', '$2a$10$p3.20U9oN7q1l/bXh8Z3Vuk84l1YdC1.Z0Y2fG0wW7R1e/pCg9/bS', 'Lê Thị Student', 'student2@example.com', '0919191919', 3, true, NOW(), NOW()),
(6, 'student3', '$2a$10$p3.20U9oN7q1l/bXh8Z3Vuk84l1YdC1.Z0Y2fG0wW7R1e/pCg9/bS', 'Phạm Văn Student', 'student3@example.com', '0929292929', 3, true, NOW(), NOW()),
(7, 'student4', '$2a$10$p3.20U9oN7q1l/bXh8Z3Vuk84l1YdC1.Z0Y2fG0wW7R1e/pCg9/bS', 'Vũ Thị Student', 'student4@example.com', '0939393939', 3, true, NOW(), NOW())
ON CONFLICT (user_id) DO NOTHING;

-- 3. Insert Mentors
INSERT INTO mentors (mentor_id, department, academic_rank, created_at, updated_at) VALUES
(2, 'Khoa Công nghệ thông tin', 'Thạc sĩ', NOW(), NOW()),
(5, 'Khoa Hệ thống thông tin', 'Tiến sĩ', NOW(), NOW())
ON CONFLICT (mentor_id) DO NOTHING;

-- 4. Insert Students
INSERT INTO students (student_id, student_code, major, class_name, date_of_birth, address, created_at, updated_at) VALUES
(3, 'SV001', 'Công nghệ thông tin', 'CNTT-K15', '2004-05-15', 'Hà Nội, Việt Nam', NOW(), NOW()),
(4, 'SV002', 'Kỹ thuật phần mềm', 'SE-K15', '2004-09-20', 'Đà Nẵng, Việt Nam', NOW(), NOW()),
(6, 'SV003', 'Hệ thống thông tin', 'HTTT-K15', '2004-03-10', 'Hải Phòng, Việt Nam', NOW(), NOW()),
(7, 'SV004', 'An toàn thông tin', 'ATTT-K15', '2004-11-25', 'Cần Thơ, Việt Nam', NOW(), NOW())
ON CONFLICT (student_id) DO NOTHING;

-- 5. Insert Internship Phases
INSERT INTO internship_phases (phase_id, phase_name, start_date, end_date, description, created_at, updated_at) VALUES
(1, 'Học kỳ Doanh nghiệp Hè 2026', '2026-06-01', '2026-08-31', 'Đợt thực tập chính thức hè dành cho sinh viên CNTT K15', NOW(), NOW()),
(2, 'Học kỳ Doanh nghiệp Thu 2026', '2026-09-01', '2026-11-30', 'Đợt thực tập chính thức thu dành cho sinh viên CNTT K15', NOW(), NOW())
ON CONFLICT (phase_id) DO NOTHING;

-- 6. Insert Evaluation Criteria
INSERT INTO evaluation_criteria (criterion_id, criterion_name, description, max_score, created_at, updated_at) VALUES
(1, 'Kiến thức chuyên môn', 'Khả năng áp dụng kiến thức kỹ thuật vào thực tế công việc', 10.00, NOW(), NOW()),
(2, 'Thái độ làm việc', 'Tác phong công nghiệp, đúng giờ, chủ động trao đổi công việc', 10.00, NOW(), NOW()),
(3, 'Báo cáo thực tập', 'Chất lượng tài liệu báo cáo và slide thuyết trình cuối đợt', 10.00, NOW(), NOW()),
(4, 'Kỹ năng làm việc nhóm', 'Khả năng giao tiếp và phối hợp với các thành viên khác trong dự án', 10.00, NOW(), NOW()),
(5, 'Khả năng giải quyết vấn đề', 'Tự tìm hiểu và đưa ra giải pháp cho các vấn đề phát sinh', 10.00, NOW(), NOW())
ON CONFLICT (criterion_id) DO NOTHING;

-- 7. Insert Assessment Rounds
INSERT INTO assessment_rounds (round_id, phase_id, round_name, start_date, end_date, description, is_active, created_at, updated_at) VALUES
(1, 1, 'Đánh giá Giữa kỳ Hè', '2026-07-10', '2026-07-20', 'Đánh giá tiến độ thực tập tuần thứ 6 đợt Hè', true, NOW(), NOW()),
(2, 1, 'Đánh giá Cuối kỳ Hè', '2026-08-25', '2026-08-31', 'Đánh giá tổng kết và chấm báo cáo thực tập đợt Hè', true, NOW(), NOW()),
(3, 2, 'Đánh giá Giữa kỳ Thu', '2026-10-10', '2026-10-20', 'Đánh giá tiến độ thực tập tuần thứ 6 đợt Thu', true, NOW(), NOW()),
(4, 2, 'Đánh giá Cuối kỳ Thu', '2026-11-25', '2026-11-30', 'Đánh giá tổng kết và chấm báo cáo thực tập đợt Thu', true, NOW(), NOW())
ON CONFLICT (round_id) DO NOTHING;

-- 8. Insert Round Criteria
-- Vòng 1 (Giữa kỳ Hè)
INSERT INTO round_criteria (round_criterion_id, round_id, criterion_id, weight, created_at, updated_at) VALUES
(1, 1, 1, 0.50, NOW(), NOW()),
(2, 1, 2, 0.50, NOW(), NOW()),
-- Vòng 2 (Cuối kỳ Hè)
(3, 2, 1, 0.40, NOW(), NOW()),
(4, 2, 2, 0.30, NOW(), NOW()),
(5, 2, 3, 0.30, NOW(), NOW()),
-- Vòng 3 (Giữa kỳ Thu)
(6, 3, 1, 0.40, NOW(), NOW()),
(7, 3, 4, 0.30, NOW(), NOW()),
(8, 3, 5, 0.30, NOW(), NOW()),
-- Vòng 4 (Cuối kỳ Thu)
(9, 4, 1, 0.30, NOW(), NOW()),
(10, 4, 2, 0.20, NOW(), NOW()),
(11, 4, 3, 0.30, NOW(), NOW()),
(12, 4, 5, 0.20, NOW(), NOW())
ON CONFLICT (round_criterion_id) DO NOTHING;

-- 9. Insert Internship Assignments
INSERT INTO internship_assignments (assignment_id, student_id, mentor_id, phase_id, assigned_date, status, created_at, updated_at) VALUES
(1, 3, 2, 1, NOW(), 'IN_PROGRESS', NOW(), NOW()),
(2, 4, 2, 1, NOW(), 'IN_PROGRESS', NOW(), NOW()),
(3, 6, 5, 2, NOW(), 'PENDING', NOW(), NOW()),
(4, 7, 5, 2, NOW(), 'IN_PROGRESS', NOW(), NOW())
ON CONFLICT (assignment_id) DO NOTHING;

-- 10. Insert Assessment Results
-- Đánh giá sinh viên 1 (Trần Văn Student) - Giữa kỳ Hè
INSERT INTO assessment_results (result_id, assignment_id, round_id, criterion_id, score, comments, evaluated_by, evaluation_date, created_at, updated_at) VALUES
(1, 1, 1, 1, 8.5, 'Làm việc tốt, có hiểu biết kiến thức nền tảng.', 2, NOW(), NOW(), NOW()),
(2, 1, 1, 2, 9.0, 'Đúng giờ, thái độ làm việc chăm chỉ, tích cực.', 2, NOW(), NOW(), NOW()),
-- Đánh giá sinh viên 1 (Trần Văn Student) - Cuối kỳ Hè
(3, 1, 2, 1, 9.0, 'Có tiến bộ vượt bậc, hoàn thành xuất sắc các task được giao.', 2, NOW(), NOW(), NOW()),
(4, 1, 2, 2, 9.5, 'Tinh thần trách nhiệm rất cao trong công việc.', 2, NOW(), NOW(), NOW()),
(5, 1, 2, 3, 8.8, 'Slide báo cáo chi tiết, thuyết trình lưu loát.', 2, NOW(), NOW(), NOW()),
-- Đánh giá sinh viên 2 (Lê Thị Student) - Giữa kỳ Hè
(6, 2, 1, 1, 7.0, 'Kiến thức kỹ thuật tạm ổn, cần cải thiện thêm kỹ năng code.', 2, NOW(), NOW(), NOW()),
(7, 2, 1, 2, 8.0, 'Hòa đồng, chịu khó lắng nghe đóng góp ý kiến.', 2, NOW(), NOW(), NOW())
ON CONFLICT (result_id) DO NOTHING;

-- 11. Đồng bộ lại PostgreSQL serial sequence sau khi import thủ công ID
SELECT setval(pg_get_serial_sequence('roles', 'id'), coalesce(max(id), 1)) FROM roles;
SELECT setval(pg_get_serial_sequence('users', 'user_id'), coalesce(max(user_id), 1)) FROM users;
SELECT setval(pg_get_serial_sequence('internship_phases', 'phase_id'), coalesce(max(phase_id), 1)) FROM internship_phases;
SELECT setval(pg_get_serial_sequence('evaluation_criteria', 'criterion_id'), coalesce(max(criterion_id), 1)) FROM evaluation_criteria;
SELECT setval(pg_get_serial_sequence('assessment_rounds', 'round_id'), coalesce(max(round_id), 1)) FROM assessment_rounds;
SELECT setval(pg_get_serial_sequence('round_criteria', 'round_criterion_id'), coalesce(max(round_criterion_id), 1)) FROM round_criteria;
SELECT setval(pg_get_serial_sequence('internship_assignments', 'assignment_id'), coalesce(max(assignment_id), 1)) FROM internship_assignments;
SELECT setval(pg_get_serial_sequence('assessment_results', 'result_id'), coalesce(max(result_id), 1)) FROM assessment_results;
