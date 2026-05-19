# Hệ Thống Quản Lý Thực Tập (Internship Management System)

Đây là hệ thống backend xây dựng trên nền tảng Spring Boot, cung cấp các API RESTful để quản lý quá trình thực tập của sinh viên. Hệ thống ứng dụng kiến trúc phân quyền chặt chẽ theo ba vai trò chính: ADMIN, MENTOR (giáo viên hướng dẫn), và STUDENT (sinh viên).

## 1. Công nghệ sử dụng

- Ngôn ngữ: Java (Spring Boot)
- Cơ sở dữ liệu: MySQL, Hibernate (Spring Data JPA)
- Bảo mật: Spring Security, JWT (JSON Web Token)
- Kiểm thử: Postman
- Build Tool: Gradle

## 2. Phân quyền hệ thống (RBAC)

Hệ thống phân cấp quyền hạn rõ ràng:
- ADMIN: Toàn quyền quản trị, tạo đợt thực tập, phân công sinh viên, tạo tiêu chí đánh giá, quản lý tài khoản.
- MENTOR: Xem danh sách sinh viên được phân công, chấm điểm và nhận xét kết quả thực tập của sinh viên.
- STUDENT: Cập nhật hồ sơ cá nhân, xem lịch sử phân công thực tập của chính mình, xem điểm số các vòng đánh giá.

## 3. Các tính năng chính

### Quản lý Người dùng và Xác thực
- Đăng ký, Đăng nhập (cấp phát JWT).
- Lấy thông tin tài khoản hiện tại.
- Admin quản lý danh sách, trạng thái tài khoản, thêm xóa sửa người dùng.

### Quản lý Hồ sơ
- Sinh viên: Cập nhật ngành học, lớp, thông tin cá nhân.
- Mentor: Cập nhật chuyên môn, phòng ban, thông tin cá nhân.

### Quản lý Đợt thực tập (Internship Phases)
- Admin định nghĩa các đợt thực tập với ngày bắt đầu, ngày kết thúc và trạng thái.

### Phân công Thực tập (Internship Assignments)
- Thể hiện mối quan hệ giữa Sinh viên, Mentor và Đợt thực tập.
- Admin thực hiện việc ghép cặp sinh viên với giáo viên hướng dẫn.
- Tính năng cập nhật trạng thái (Đang chờ, Đang thực hiện, Hoàn thành, Đã hủy).

### Quản lý Tiêu chí và Vòng đánh giá
- Tiêu chí đánh giá (Evaluation Criteria): Các mục tiêu chuẩn cần chấm điểm.
- Vòng đánh giá (Assessment Rounds): Các giai đoạn đánh giá.
- Tiêu chí vòng đánh giá (Round Criteria): Admin lập map giữa các vòng và các tiêu chí với trọng số riêng biệt.

### Chấm điểm Thực tập (Assessment Results)
- Mentor gọi API ghi nhận kết quả đánh giá của từng sinh viên trên các tiêu chí của các vòng.
- Hệ thống tự động xác thực Mentor chỉ có thể chấm điểm cho những Sinh viên đang được giao hướng dẫn.
- Sinh viên có thể xem được bảng điểm sau khi Mentor đánh giá.

## 4. Luồng hoạt động tiêu biểu (Workflow)

Bước 1: Đăng ký và Đăng nhập
- Admin tạo các tài khoản người dùng (kèm role).
- Sinh viên và Mentor đăng nhập vào hệ thống để nhận Token, dùng Token này cho mọi API khác.
- Người dùng hoàn thiện thông tin hồ sơ (Profile) ứng với Role của mình.

Bước 2: Khởi tạo thông số hệ thống
- Admin tạo Đợt thực tập mới.
- Admin tạo ra Vòng đánh giá và bộ Tiêu chí.

Bước 3: Phân công
- Admin thực hiện phân công Sinh viên vào Đợt thực tập cùng với Mentor hướng dẫn. Hệ thống sinh ra một bản ghi Assignment.

Bước 4: Chấm điểm
- Đến kỳ đánh giá, Mentor đăng nhập, xem danh sách sinh viên được giao.
- Mentor gọi API tạo Result để chấm điểm kèm nhận xét cho từng sinh viên theo từng tiêu chí của vòng.

Bước 5: Xem kết quả
- Sinh viên đăng nhập, vào API xem kết quả để biết điểm số và lời nhận xét từ Mentor.

## 6. Quy chuẩn lỗi và Mã trạng thái (HTTP Status Codes)

Hệ thống xây dựng cơ chế bắt lỗi đồng nhất với GlobalExceptionHandler, các mã trạng thái sử dụng bao gồm:
- 200 OK: Truy vấn hoặc cập nhật dữ liệu thành công.
- 201 Created: Tạo mới dữ liệu (POST) thành công.
- 400 Bad Request: Dữ liệu đầu vào sai lệch định dạng (Validation Exception).
- 401 Unauthorized: Thiếu JWT hoặc token hết hạn.
- 403 Forbidden: Truy cập vào API không đúng với Role của mình, hoặc cố ý truy cập vào tài nguyên của người khác.
- 404 Not Found: Không tìm thấy bản ghi theo yêu cầu.
- 409 Conflict: Xung đột logic (Tên đăng nhập tồn tại, phân công bị trùng).
- 500 Internal Server Error: Lỗi logic hệ thống hoặc các ngoại lệ chưa được bắt.
