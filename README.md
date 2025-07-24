# TinhTX Player

[![GitHub](https://img.shields.io/badge/GitHub-tinhtx%2Fplayer-blue)](https://github.com/tinhtx/player)
[![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](https://opensource.org/licenses/Apache-2.0)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Compose-BOM%202024.10-orange.svg)](https://developer.android.com/jetpack/compose)

**TinhTX Player** là một trình phát media (nhạc & video) hiện đại, được xây dựng cho nền tảng Android với triết lý **"local-first"** và **"privacy-first"**. Dự án này không chỉ là một ứng dụng nghe nhạc, mà còn là một minh chứng (showcase) về việc áp dụng các phương pháp tốt nhất trong phát triển Android, bao gồm Clean Architecture, Jetpack Compose, Material 3 và nhiều công nghệ tiên tiến khác.

## ✨ Tính Năng Nổi Bật

* **🔒 Ưu tiên Cục bộ & Quyền riêng tư**: Quét và quản lý media trực tiếp trên thiết bị thông qua `MediaStore`. Không thu thập dữ liệu người dùng, không yêu cầu quyền truy cập file không cần thiết.
* **🚀 Hiệu suất cao**: Sử dụng **ExoPlayer** được tinh chỉnh để giảm thiểu re-buffering, kết hợp với các kỹ thuật tối ưu Jetpack Compose để đảm bảo trải nghiệm mượt mà.
* **🎨 Giao diện Thích ứng Đa dạng**: Tích hợp Material You (Dynamic Color) và cung cấp các bộ theme tùy biến cho nhiều nhóm đối tượng (Child, Teen, Adult), đảm bảo tính khả dụng và dễ tiếp cận.
* **🏗️ Kiến trúc Vững chắc**: Được xây dựng trên nền tảng **Clean Architecture** và **MVVM**, giúp việc bảo trì, kiểm thử và mở rộng tính năng trở nên dễ dàng.

## 🏛️ Kiến Trúc Tổng Thể

Dự án tuân thủ nghiêm ngặt **Clean Architecture** và được phân tách thành nhiều module Gradle, đảm bảo sự độc lập và rõ ràng giữa các lớp.

* **Luồng dữ liệu**: Đơn hướng (Unidirectional Data Flow - UDF), trong đó UI chỉ nhận trạng thái từ ViewModel và gửi sự kiện lên, đảm bảo tính nhất quán và dễ dự đoán.
* **Nguyên tắc phụ thuộc**: `Presentation` → `Domain` → `Data`. Các lớp bên trong không được biết đến sự tồn tại của các lớp bên ngoài.

### Cấu trúc Module

Dưới đây là mô tả vai trò của từng module trong dự án, tương ứng với cấu trúc thư mục thực tế:

| Module | Tên thư mục | Vai trò và Trách nhiệm |
| :--- | :--- | :--- |
| **Application** | `:app` | Lắp ráp tất cả các module khác lại với nhau. Chứa `MainActivity`, lớp `Application`, và các module DI (Hilt) khởi tạo toàn cục. |
| **Core** | `:core` | Chứa các mã nguồn dùng chung không thuộc về một logic cụ thể nào: các lớp tiện ích, hằng số, điều hướng, và các thành phần cơ bản khác. |
| **Data** | `:data` | Chịu trách nhiệm cung cấp dữ liệu. Chứa các lớp triển khai `Repository` từ `domain`, DAO (Room), các lớp làm việc với `MediaStore` và `Preferences`. |
| **Domain** | `:domain` | "Trái tim" của ứng dụng, chứa toàn bộ logic nghiệp vụ. Gồm các `UseCase`, `interface` của `Repository`, và các model dữ liệu thuần Kotlin. Module này **không phụ thuộc** vào Android SDK. |
| **Presentation** | `:presentation` | Chịu trách nhiệm về tầng giao diện người dùng (UI). Chứa các màn hình (Screens), `ViewModel`, `Composable` components, và các tài nguyên liên quan đến theme. |
| **Media** | `:media` | Đóng gói tất cả logic phức tạp liên quan đến việc phát media. Quản lý `ExoPlayer`, `MediaSessionService`, thông báo (notification), hàng đợi phát (queue) và bộ chỉnh âm (equalizer). |
| **Feature** | `:feature` | Chứa các tính năng độc lập, có thể tách rời như Picture-in-Picture (`:pip`) hoặc Widget (`:widget`). |

### Bảng Phân rã Thành phần

| Tầng (Layer) | Thành phần then chốt | Mục đích | Công nghệ |
| :--- | :--- | :--- | :--- |
| **Presentation** | Jetpack Compose UI | Xây dựng màn hình & component Material 3 | Compose + Material 3 |
| **Domain** | Use Cases | Điều phối logic nghiệp vụ | Kotlin + Coroutines |
| **Data** | Room Database | Lưu trữ metadata, playlist | SQLCipher (tùy chọn) |
| **Media** | ExoPlayer Integration | Phát nhạc/video, crossfade | ExoPlayer 2.19.1 |
| **Cross-cutting** | Hilt DI | Quản lý phụ thuộc | Dagger-Hilt |

## 🚀 Công nghệ & Thư viện

* **Ngôn ngữ**: [Kotlin](https://kotlinlang.org/)
* **Kiến trúc**: MVVM, Clean Architecture, UDF
* **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) & [Material 3](https://m3.material.io/)
* **Bất đồng bộ**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://developer.android.com/kotlin/flow)
* **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
* **Media Playback**: [ExoPlayer (Media3)](https://developer.android.com/media/media3/exoplayer)
* **Lưu trữ cục bộ**: [Room](https://developer.android.com/training/data-storage/room), [DataStore](https://developer.android.com/topic/libraries/architecture/datastore), `EncryptedSharedPreferences`
* **Kiểm thử**: JUnit5, Turbine, Robolectric, ComposeTestRule, MacroBenchmark

## 🧪 Chiến lược Kiểm thử

Dự án đặt mục tiêu bao phủ test ở nhiều cấp độ để đảm bảo chất lượng và sự ổn định.

| Cấp độ | Công cụ | Trọng tâm | Ghi chú |
| :--- | :--- | :--- | :--- |
| **Unit** | JUnit5 + Turbine | UseCase, ViewModel | Sử dụng `StandardTestDispatcher` |
| **Integration** | Robolectric | DAO, Repository | Stub cho SQLCipher |
| **UI** | `ComposeTestRule` | Tương tác người dùng, semantics | Sử dụng test-tag & state restoration |
| **Media** | ExoPlayerTestRunner | Buffer underrun, crossfade | Sử dụng `FakeMediaSource` |
| **E2E** | Firebase Test Lab | Đa thiết bị | Kịch bản Espresso cho playback |

## 🛣️ Lộ trình Phát triển

1.  **Sprint 1**: Thiết lập project, cấu trúc thư mục, DI, database trống.
2.  **Sprint 2**: Hoàn thiện `MediaStoreScanner` & `HomeScreen` hiển thị danh sách media.
3.  **Sprint 3**: Hoàn thiện `ExoPlayerManager`, mini-player, và notification.
4.  **Sprint 4**: Xây dựng `MusicPlayerScreen` với equalizer 10-band.
5.  **Sprint 5**: Xây dựng `VideoPlayerScreen` với chế độ PiP và điều khiển bằng cử chỉ.
6.  **Sprint 6**: Màn hình Cài đặt, theme theo nhóm tuổi, tính năng backup/restore.
7.  **Sprint 7**: Testing & benchmarking, phát hành phiên bản beta nội bộ.
8.  **Sprint 8**: Tăng cường bảo mật, chuẩn bị và xuất bản mã nguồn mở (Apache 2.0).

## 🚀 Bắt đầu

Để xây dựng và chạy dự án, bạn cần:

* Android Studio Iguana (hoặc mới hơn)
* JDK 17

Clone repository và chạy lệnh sau từ thư mục gốc của dự án:

```bash
# Build dự án
./gradlew build

# Cài đặt ứng dụng lên thiết bị/máy ảo
./gradlew installDebug
```

## 🤝 Đóng góp

Mọi đóng góp đều được hoan nghênh! Vui lòng tạo một `Issue` để thảo luận về thay đổi bạn muốn thực hiện hoặc gửi một `Pull Request`.

## 📄 Bản quyền

Dự án này được cấp phép theo **Giấy phép Apache 2.0**. Xem file `LICENSE` để biết thêm chi tiết.