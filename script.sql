USE [master]
GO
/****** Object:  Database [QLDT]    Script Date: 12/2/2021 10:30:27 AM ******/
CREATE DATABASE [QLDT]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'QLDT', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL15.MSSQLSERVER\MSSQL\DATA\QLDT.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'QLDT_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL15.MSSQLSERVER\MSSQL\DATA\QLDT_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO
ALTER DATABASE [QLDT] SET COMPATIBILITY_LEVEL = 150
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [QLDT].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [QLDT] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [QLDT] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [QLDT] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [QLDT] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [QLDT] SET ARITHABORT OFF 
GO
ALTER DATABASE [QLDT] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [QLDT] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [QLDT] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [QLDT] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [QLDT] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [QLDT] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [QLDT] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [QLDT] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [QLDT] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [QLDT] SET  ENABLE_BROKER 
GO
ALTER DATABASE [QLDT] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [QLDT] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [QLDT] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [QLDT] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [QLDT] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [QLDT] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [QLDT] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [QLDT] SET RECOVERY FULL 
GO
ALTER DATABASE [QLDT] SET  MULTI_USER 
GO
ALTER DATABASE [QLDT] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [QLDT] SET DB_CHAINING OFF 
GO
ALTER DATABASE [QLDT] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [QLDT] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [QLDT] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [QLDT] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'QLDT', N'ON'
GO
ALTER DATABASE [QLDT] SET QUERY_STORE = OFF
GO
USE [QLDT]
GO
/****** Object:  Table [dbo].[BoDe]    Script Date: 12/2/2021 10:30:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BoDe](
	[ma] [int] IDENTITY(1,1) NOT NULL,
	[ten] [nvarchar](200) NOT NULL,
	[thoi_gian_thi] [int] NOT NULL,
	[email_tao] [varchar](255) NOT NULL,
 CONSTRAINT [BoDe_PK] PRIMARY KEY CLUSTERED 
(
	[ma] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Cau]    Script Date: 12/2/2021 10:30:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Cau](
	[ma_BD] [int] NOT NULL,
	[cau_hoi] [nvarchar](1000) NOT NULL,
	[phuong_an_1] [nvarchar](1000) NOT NULL,
	[phuong_an_2] [nvarchar](1000) NOT NULL,
	[phuong_an_3] [nvarchar](1000) NOT NULL,
	[phuong_an_4] [nvarchar](1000) NOT NULL,
	[dap_an] [nvarchar](1000) NOT NULL,
	[id] [int] IDENTITY(1,1) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ThongTinThi]    Script Date: 12/2/2021 10:30:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ThongTinThi](
	[email_thi] [varchar](255) NOT NULL,
	[ma_BD] [int] NOT NULL,
	[diem] [float] NOT NULL,
	[thoi_gian_hoan_tat] [int] NOT NULL,
 CONSTRAINT [ThongTinThi_PK] PRIMARY KEY CLUSTERED 
(
	[email_thi] ASC,
	[ma_BD] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Users]    Script Date: 12/2/2021 10:30:28 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Users](
	[email] [varchar](255) NOT NULL,
	[password] [char](255) NOT NULL,
	[ho_ten] [nvarchar](255) NOT NULL,
	[gioi_tinh] [char](3) NULL,
	[ngay_sinh] [date] NULL,
	[status] [int] NULL,
 CONSTRAINT [Users_PK] PRIMARY KEY CLUSTERED 
(
	[email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[BoDe] ON 

INSERT [dbo].[BoDe] ([ma], [ten], [thoi_gian_thi], [email_tao]) VALUES (2002, N'Trắc nghiệm Pascal', 15, N'fejak77963@terasd.com')
INSERT [dbo].[BoDe] ([ma], [ten], [thoi_gian_thi], [email_tao]) VALUES (2003, N'Đề kiểm tra 15 phút môn lịch sử lớp 9', 15, N'fejak77963@terasd.com')
SET IDENTITY_INSERT [dbo].[BoDe] OFF
GO
SET IDENTITY_INSERT [dbo].[Cau] ON 

INSERT [dbo].[Cau] ([ma_BD], [cau_hoi], [phuong_an_1], [phuong_an_2], [phuong_an_3], [phuong_an_4], [dap_an], [id]) VALUES (2003, N'Tình hình nổi bật của châu Á trước Chiến tranh thế giới thứ hai là gì?', N'Tất cả các nước châu Á đều là nước độc lập.', N'Hầu hết các nước châu Á đều sự bóc lột, nô dịch của các nước đế quốc thực dân.', N'Các nước châu Á đều là thuộc địa kiểu mới của Mĩ.', N'Các nước châu Á nằm trong mặt trận Đồng minh chóng phát xít và đã giành được độc lập.', N'2', 2012)
INSERT [dbo].[Cau] ([ma_BD], [cau_hoi], [phuong_an_1], [phuong_an_2], [phuong_an_3], [phuong_an_4], [dap_an], [id]) VALUES (2003, N'Hãy cho biết nội dung nào KHÔNG PHẢI của tình hình các nước châu Á sau khi giành độc lập?', N'Tất cả các nước châu Á đều ổn định và phát triển.', N'Diễn ra nhiều cuộc chiến tranh xâm lược của các nước đế quốc.', N'Một số nước diễn ra những cuộc xung đội tranh chấp biên giới lãnh thổ hoặc phong trào li khai.', N'Các nước đế quốc thực dân cố duy trì ách thống trị.', N'1', 2013)
INSERT [dbo].[Cau] ([ma_BD], [cau_hoi], [phuong_an_1], [phuong_an_2], [phuong_an_3], [phuong_an_4], [dap_an], [id]) VALUES (2003, N'Kết quả của cuộc nội chiến ở Trung Quốc giữa Quốc dân đảng và Đảng Cộng sản Trung Quốc từ năm 1946 đến năm 1949 như thế nào?', N'Đảng Cộng sản Trung Quốc bước đầu giành thắng lợi.', N'Hai bên tiếp tục hòa hoãn.', N'Tập đoàn Tưởng Giới Thạch thua chạy ra Đài Loan.', N'Đảng Cộng sản Trung Quốc thu hẹp vùng giải phóng.', N'3', 2014)
INSERT [dbo].[Cau] ([ma_BD], [cau_hoi], [phuong_an_1], [phuong_an_2], [phuong_an_3], [phuong_an_4], [dap_an], [id]) VALUES (2003, N'Sau khi nước Cộng hòa Nhân dân Trung Hoa ra đời, nhiệm vụ to lớn nhất của nhan dân Trung Quốc là gì?', N'Tiến hành cuộc chiến tranh xâm lược.', N'Đầu tư hiện đại hóa quân đội.', N'Đưa đất nước thoát khỏi nghèo nàn, lạc hậu, tiến hành công nghiệp hóa, phát triển kinh tế, xã hội.', N'tất cả đều sai', N'3', 2015)
INSERT [dbo].[Cau] ([ma_BD], [cau_hoi], [phuong_an_1], [phuong_an_2], [phuong_an_3], [phuong_an_4], [dap_an], [id]) VALUES (2003, N'Sự ra đời của nước Cộng hòa Nhân dân Trung Hoa có ý nghĩa quốc tế như thế nào?', N'Hệ thống chủ nghĩa xã hội được nối liền từ châu Âu sang châu á.', N'Đưa nước Trung Hoa bước vào kỉ nguyên độc lập tự do, tiến lên chủ nghĩa xã hội.', N'Kết thúc hơn 100 năm nô dịch và thống trị của đế quốc đối với nhân dân Trung Hoa.', N'Báo hiệu sự kết thúc ách thống trị, nô dịch của chế độ phong kiến và tư bản trên đất Trung Hoa.', N'1', 2016)
INSERT [dbo].[Cau] ([ma_BD], [cau_hoi], [phuong_an_1], [phuong_an_2], [phuong_an_3], [phuong_an_4], [dap_an], [id]) VALUES (2003, N'Nội dung nào sau đây KHÔNG thuộc đường lối cải cách – mở cửa của Trung Quốc từ năm 1978?', N'Tiến hành cải cách và mở cửa.', N'Lấy phát triển kinh tế làm trung tâm.', N'Thực hiện đường lối “ba ngọn cờ hồng”.', N'Chuyển nền kinh tế kế hoạch hóa tập trung sang nền kinh tế thị trường xã hội chủ nghĩa.', N'3', 2017)
INSERT [dbo].[Cau] ([ma_BD], [cau_hoi], [phuong_an_1], [phuong_an_2], [phuong_an_3], [phuong_an_4], [dap_an], [id]) VALUES (2003, N'Lĩnh vực nào được coi là trọng tâm trong đường lối cải cách – mở cửa ở Trung Quốc năm 1978?', N'Chính trị.', N'Kinh tế.', N'Văn hóa – giáo dục.', N'Khoa học – kĩ thuật.', N'2', 2018)
INSERT [dbo].[Cau] ([ma_BD], [cau_hoi], [phuong_an_1], [phuong_an_2], [phuong_an_3], [phuong_an_4], [dap_an], [id]) VALUES (2003, N'Chủ trương cải cách – mở cửa của Trung ương Đảng Cộng sản Trung Quốc được đề ra tại?', N'Đại hội cách mạng vô sản (1966-1976).', N'Hội nghị Trung ương Đảng Cộng sản Trung Quốc (12/1976).', N'Đại hội Đảng Cộng sản Trung Quốc lần thứ XII (9/1982).', N'Đại hội Đảng Cộng sản Trung Quốc lần thứ XIII (10/1987).', N'2', 2019)
INSERT [dbo].[Cau] ([ma_BD], [cau_hoi], [phuong_an_1], [phuong_an_2], [phuong_an_3], [phuong_an_4], [dap_an], [id]) VALUES (2003, N'Từ cuối những năm 80 của thế kỉ XX đến nay, chính sách đối ngoại của Trung Quốc là', N'thực hiện đường lối đối ngoại bất lợi cho cách mạng Trung Quốc.', N'bắt tay với Mĩ chống lại Liên Xô.', N'hợp tác với các nước ASEAN để cùng phát triển.', N'mở rộng quan hệ hữu nghị, hợp tác với các nước trên thế giới.', N'4', 2020)
INSERT [dbo].[Cau] ([ma_BD], [cau_hoi], [phuong_an_1], [phuong_an_2], [phuong_an_3], [phuong_an_4], [dap_an], [id]) VALUES (2003, N'Một trong những biến đổi lớn về chính trị của khu vực Đông Bắc Á sau Chiến tranh thế giới thứ hai là', N'cuộc nội chiến ở Trung Quốc (1946 – 1949).', N'Sự thành lập hai nhà nước trên bán đảo Triều Tiên.', N'Nước Cộng hòa Nhân dân Trung Hoa ra đời.', N'Trung Quốc thu hồi Hồng Kông, Ma Cao.', N'3', 2021)
INSERT [dbo].[Cau] ([ma_BD], [cau_hoi], [phuong_an_1], [phuong_an_2], [phuong_an_3], [phuong_an_4], [dap_an], [id]) VALUES (2002, N'Chương trình máy tính được theo các bước:', N'Viết chương trình bằng ngôn ngữ lập trình', N'Dịch chương trình thành ngôn ngữ máy', N'Viết chương trình bằng ngôn ngữ lập trình rồi dịch chương trình thành ngôn ngữ máy', N'Viết chương trình trên giấy rồi gõ vào máy tính', N'3', 2022)
INSERT [dbo].[Cau] ([ma_BD], [cau_hoi], [phuong_an_1], [phuong_an_2], [phuong_an_3], [phuong_an_4], [dap_an], [id]) VALUES (2002, N'Tại sao cần viết chương trình?', N'viết chương trình giúp con người', N'điều khiển máy tính', N'một cách đơn giản và hiệu quả hơn', N'Tất cả đều sai', N'4', 2023)
INSERT [dbo].[Cau] ([ma_BD], [cau_hoi], [phuong_an_1], [phuong_an_2], [phuong_an_3], [phuong_an_4], [dap_an], [id]) VALUES (2002, N'Con người chỉ dẫn cho máy tính thực hiện công việc như thế nào?', N'thông qua một từ khóa', N'thông qua các tên', N'thông qua các lệnh', N'thông qua một hằng', N'3', 2024)
INSERT [dbo].[Cau] ([ma_BD], [cau_hoi], [phuong_an_1], [phuong_an_2], [phuong_an_3], [phuong_an_4], [dap_an], [id]) VALUES (2002, N'Viết chương trình là:', N'hướng dẫn máy tính', N'thực hiện các công việc', N'hay giải một bài toán cụ thể', N'Tất cả đều sai', N'4', 2025)
INSERT [dbo].[Cau] ([ma_BD], [cau_hoi], [phuong_an_1], [phuong_an_2], [phuong_an_3], [phuong_an_4], [dap_an], [id]) VALUES (2002, N'Theo em hiểu viết chương trình là :', N'Tạo ra các câu lệnh được sắp xếp theo một trình tự nào đó', N'Viết ra một đoạn văn bản được sắp xếp theo chương trình', N'Viết ra các câu lệnh mà em đã được học', N'Tạo ra các câu lệnh để điều khiển Robot', N'1', 2026)
INSERT [dbo].[Cau] ([ma_BD], [cau_hoi], [phuong_an_1], [phuong_an_2], [phuong_an_3], [phuong_an_4], [dap_an], [id]) VALUES (2002, N'Ngôn ngữ lập trình là:', N'ngôn ngữ dùng để viết một chương trình máy tính', N'ngôn ngữ dùng để viết các chương trình máy tính', N'các dãy bit (dãy các số chỉ gồm 0 và 1)', N'chương trình dịch', N'2', 2027)
INSERT [dbo].[Cau] ([ma_BD], [cau_hoi], [phuong_an_1], [phuong_an_2], [phuong_an_3], [phuong_an_4], [dap_an], [id]) VALUES (2002, N'Môi trường lập trình gồm:', N'chương trình soạn thảo', N'chương trình dịch', N'các công cụ trợ giúp tìm kiếm, sửa lỗi…', N'Tất cả đều đúng', N'4', 2028)
INSERT [dbo].[Cau] ([ma_BD], [cau_hoi], [phuong_an_1], [phuong_an_2], [phuong_an_3], [phuong_an_4], [dap_an], [id]) VALUES (2002, N'Ngôn ngữ được sử dụng để viết chương trình là:', N'Ngôn ngữ lập trình', N'Ngôn ngữ máy', N'Ngôn ngữ tự nhiên', N'Ngôn ngữ tiếng Việt', N'1', 2029)
INSERT [dbo].[Cau] ([ma_BD], [cau_hoi], [phuong_an_1], [phuong_an_2], [phuong_an_3], [phuong_an_4], [dap_an], [id]) VALUES (2002, N'Chương trình dịch dùng để:', N'Dịch từ ngôn ngữ lập trình sang ngôn ngữ máy', N'Dịch từ ngôn ngữ lập trình sang ngôn ngữ tự nhiên', N'Dịch từ ngôn ngữ máy sang ngôn ngữ lập trình', N'Dịch từ ngôn ngữ máy sang ngôn ngữ tự nhiên', N'1', 2030)
INSERT [dbo].[Cau] ([ma_BD], [cau_hoi], [phuong_an_1], [phuong_an_2], [phuong_an_3], [phuong_an_4], [dap_an], [id]) VALUES (2002, N'Ngôn ngữ lập máy là:', N'ngôn ngữ dùng để viết một chương trình máy tính', N'ngôn ngữ dùng để viết các chương trình máy tính', N'các câu lệnh được tạo ra từ hai số 1 và 0', N'chương trình dịch', N'3', 2031)
SET IDENTITY_INSERT [dbo].[Cau] OFF
GO
INSERT [dbo].[ThongTinThi] ([email_thi], [ma_BD], [diem], [thoi_gian_hoan_tat]) VALUES (N'hackerno1@gmail.com', 2002, 2, 15)
INSERT [dbo].[ThongTinThi] ([email_thi], [ma_BD], [diem], [thoi_gian_hoan_tat]) VALUES (N'hackerno1@gmail.com', 2003, 2, 19)
INSERT [dbo].[ThongTinThi] ([email_thi], [ma_BD], [diem], [thoi_gian_hoan_tat]) VALUES (N'payakar946@tinydef.com', 2002, 5, 23)
INSERT [dbo].[ThongTinThi] ([email_thi], [ma_BD], [diem], [thoi_gian_hoan_tat]) VALUES (N'payakar946@tinydef.com', 2003, 1, 18)
INSERT [dbo].[ThongTinThi] ([email_thi], [ma_BD], [diem], [thoi_gian_hoan_tat]) VALUES (N'user1@gmail.com', 2002, 3, 17)
INSERT [dbo].[ThongTinThi] ([email_thi], [ma_BD], [diem], [thoi_gian_hoan_tat]) VALUES (N'user1@gmail.com', 2003, 2, 15)
INSERT [dbo].[ThongTinThi] ([email_thi], [ma_BD], [diem], [thoi_gian_hoan_tat]) VALUES (N'user2@gmail.com', 2002, 3, 17)
INSERT [dbo].[ThongTinThi] ([email_thi], [ma_BD], [diem], [thoi_gian_hoan_tat]) VALUES (N'user2@gmail.com', 2003, 4, 16)
INSERT [dbo].[ThongTinThi] ([email_thi], [ma_BD], [diem], [thoi_gian_hoan_tat]) VALUES (N'user3@gmail.com', 2002, 2, 17)
INSERT [dbo].[ThongTinThi] ([email_thi], [ma_BD], [diem], [thoi_gian_hoan_tat]) VALUES (N'user3@gmail.com', 2003, 3, 17)
INSERT [dbo].[ThongTinThi] ([email_thi], [ma_BD], [diem], [thoi_gian_hoan_tat]) VALUES (N'user4@gmail.com', 2002, 3, 15)
INSERT [dbo].[ThongTinThi] ([email_thi], [ma_BD], [diem], [thoi_gian_hoan_tat]) VALUES (N'user4@gmail.com', 2003, 2, 16)
INSERT [dbo].[ThongTinThi] ([email_thi], [ma_BD], [diem], [thoi_gian_hoan_tat]) VALUES (N'user5@gmail.com', 2002, 3, 18)
INSERT [dbo].[ThongTinThi] ([email_thi], [ma_BD], [diem], [thoi_gian_hoan_tat]) VALUES (N'vucaoit@gmail.com', 2002, 7, 109)
INSERT [dbo].[ThongTinThi] ([email_thi], [ma_BD], [diem], [thoi_gian_hoan_tat]) VALUES (N'vucaoit@gmail.com', 2003, 5, 29)
GO
INSERT [dbo].[Users] ([email], [password], [ho_ten], [gioi_tinh], [ngay_sinh], [status]) VALUES (N'fejak77963@terasd.com', N'f4151f621c1cc3383217f0ce4ff5c0b6                                                                                                                                                                                                                               ', N'Nguyễn Đức Anh Tài', N'nam', CAST(N'2003-08-20' AS Date), 0)
INSERT [dbo].[Users] ([email], [password], [ho_ten], [gioi_tinh], [ngay_sinh], [status]) VALUES (N'hackerno1@gmail.com', N'f4151f621c1cc3383217f0ce4ff5c0b6                                                                                                                                                                                                                               ', N'héc cơ nasa', N'nam', CAST(N'2004-09-09' AS Date), 0)
INSERT [dbo].[Users] ([email], [password], [ho_ten], [gioi_tinh], [ngay_sinh], [status]) VALUES (N'payakar946@tinydef.com', N'f4151f621c1cc3383217f0ce4ff5c0b6                                                                                                                                                                                                                               ', N'Ngô Thừa Ân', N'nam', CAST(N'2001-12-19' AS Date), 0)
INSERT [dbo].[Users] ([email], [password], [ho_ten], [gioi_tinh], [ngay_sinh], [status]) VALUES (N'user1@gmail.com', N'f4151f621c1cc3383217f0ce4ff5c0b6                                                                                                                                                                                                                               ', N'Đoàn Thị Thẩm', N'nu ', CAST(N'2021-12-13' AS Date), 0)
INSERT [dbo].[Users] ([email], [password], [ho_ten], [gioi_tinh], [ngay_sinh], [status]) VALUES (N'user2@gmail.com', N'f4151f621c1cc3383217f0ce4ff5c0b6                                                                                                                                                                                                                               ', N'Cao vip pro', N'nam', CAST(N'2000-01-01' AS Date), 0)
INSERT [dbo].[Users] ([email], [password], [ho_ten], [gioi_tinh], [ngay_sinh], [status]) VALUES (N'user3@gmail.com', N'f4151f621c1cc3383217f0ce4ff5c0b6                                                                                                                                                                                                                               ', N'Học sinh giỏi tỉnh', N'nu ', CAST(N'2003-01-01' AS Date), 0)
INSERT [dbo].[Users] ([email], [password], [ho_ten], [gioi_tinh], [ngay_sinh], [status]) VALUES (N'user4@gmail.com', N'f4151f621c1cc3383217f0ce4ff5c0b6                                                                                                                                                                                                                               ', N'Best toán', N'nam', CAST(N'2005-01-05' AS Date), 0)
INSERT [dbo].[Users] ([email], [password], [ho_ten], [gioi_tinh], [ngay_sinh], [status]) VALUES (N'user5@gmail.com', N'f4151f621c1cc3383217f0ce4ff5c0b6                                                                                                                                                                                                                               ', N'Nopita', N'nam', CAST(N'2009-05-05' AS Date), 0)
INSERT [dbo].[Users] ([email], [password], [ho_ten], [gioi_tinh], [ngay_sinh], [status]) VALUES (N'vucaoit@gmail.com', N'f4151f621c1cc3383217f0ce4ff5c0b6                                                                                                                                                                                                                               ', N'Vũ Đình Cao', N'nam', CAST(N'2000-10-16' AS Date), 0)
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__Users__AB6E6164AFA43E6A]    Script Date: 12/2/2021 10:30:28 AM ******/
ALTER TABLE [dbo].[Users] ADD UNIQUE NONCLUSTERED 
(
	[email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[Users] ADD  CONSTRAINT [df_status_default]  DEFAULT ('0') FOR [status]
GO
ALTER TABLE [dbo].[BoDe]  WITH CHECK ADD  CONSTRAINT [BoDe_FK] FOREIGN KEY([email_tao])
REFERENCES [dbo].[Users] ([email])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[BoDe] CHECK CONSTRAINT [BoDe_FK]
GO
ALTER TABLE [dbo].[Cau]  WITH CHECK ADD  CONSTRAINT [Cau_FK_1] FOREIGN KEY([ma_BD])
REFERENCES [dbo].[BoDe] ([ma])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[Cau] CHECK CONSTRAINT [Cau_FK_1]
GO
ALTER TABLE [dbo].[ThongTinThi]  WITH CHECK ADD  CONSTRAINT [ThongTinThi_FK_1] FOREIGN KEY([email_thi])
REFERENCES [dbo].[Users] ([email])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[ThongTinThi] CHECK CONSTRAINT [ThongTinThi_FK_1]
GO
ALTER TABLE [dbo].[ThongTinThi]  WITH CHECK ADD  CONSTRAINT [ThongTinThi_FK_2] FOREIGN KEY([ma_BD])
REFERENCES [dbo].[BoDe] ([ma])
GO
ALTER TABLE [dbo].[ThongTinThi] CHECK CONSTRAINT [ThongTinThi_FK_2]
GO
USE [master]
GO
ALTER DATABASE [QLDT] SET  READ_WRITE 
GO
