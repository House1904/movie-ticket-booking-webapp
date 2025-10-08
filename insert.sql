INSERT INTO movie (title, description, duration, ageLimit, releaseDate, language, posterUrl, trailerUrl, actor)
VALUES 
('Avengers: Endgame', 
 'The Avengers assemble for one final stand against Thanos.',
 181, 'PG-13', '2019-04-26 00:00:00', 'English',
 'https://static.wikia.nocookie.net/marvelcinematicuniverse/images/9/91/Endgame_Poster_2.jpg/revision/latest?cb=20190314215527', 
 'https://youtube.com/watch?v=TcMBFSGVi1c', 
 'Robert Downey Jr., Chris Evans');

INSERT INTO movie (title, description, duration, ageLimit, releaseDate, language, posterUrl, trailerUrl, actor)
VALUES 
('Chị Ngã Em Nâng', 
 'Chị Ngã Em Nâng là câu chuyện cảm động xoay quanh hai chị em Thương và Lực – lớn lên trong một gia đình gắn bó với nghề làm nhang truyền thống. ',
 120, 'T13', '2025-03-10 00:00:00', 'Vietnamese',
 'https://iguov8nhvyobj.vcdn.cloud/media/catalog/product/cache/1/image/c5f0a1eff4c394a251036189ccddaacd/c/n/cnen-mainposter-1.jpg', 
 'https://youtu.be/I6ez5Rf43tI', 
 'Lê Khánh, Quốc Trường, Thuận Nguyễn, Uyển Ân');

INSERT INTO movie (title, description, duration, ageLimit, releaseDate, language, posterUrl, trailerUrl, actor)
VALUES 
('Mưa Đỏ', 
 'Phim truyện điện ảnh Mưa Đỏ nói về đề tài chiến tranh cách mạng trận Thành Cổ Quảng Trị',
 124, 'T13', '2019-05-30 00:00:00', 'Vietnamese',
 'https://cdn.nhandan.vn/images/4655dea7deebadd3e7b51fbe3a4f19d5e91a76373e439bafb34808b901efea23ff03b4f191ececfb0cc7c8e51a28c837/muado-binh.jpg', 
 'https://youtube.com/watch?v=SEUXfv87Wpk', 
 'Đỗ Nhật Hoàng, Steven Nguyễn');

INSERT INTO movie (title, description, duration, ageLimit, releaseDate, language, posterUrl, trailerUrl, actor)
VALUES 
('Tử Chiến Trên Không', 
 'Vụ cướp máy bay.',
 118, 'T16', '2025-09-19 00:00:00', 'Vietnamese',
 'https://cinestar.com.vn/_next/image/?url=https%3A%2F%2Fapi-website.cinestar.com.vn%2Fmedia%2Fwysiwyg%2FPosters%2F09-2025%2Ftu-chien-tren-khong-poster.jpg', 
 'https://youtube.com/watch?v=U2Qp5pL3ovA', 
 'Thái Hòa, Kaity Nguyễn');
--Insert vào bảng cinema
INSERT INTO cinema (name, address, phone, createat)
VALUES
('CGV Vincom Đồng Khởi', 'Vincom Center, 72 Lê Thánh Tôn, Quận 1, TP.HCM', '02838237456', '2023-09-10 09:00:00'),

('Lotte Cinema Gò Vấp', '242 Nguyễn Văn Lượng, Quận Gò Vấp, TP.HCM', '02835894711', '2023-09-15 10:30:00'),

('Galaxy Nguyễn Du', '116 Nguyễn Du, Quận 1, TP.HCM', '02838226688', '2023-09-20 08:45:00'),

('BHD Star Bitexco', 'L3 Bitexco Tower, 2 Hải Triều, Quận 1, TP.HCM', '02839158000', '2023-09-25 11:15:00'),

('Cinestar Quốc Thanh', '271 Nguyễn Trãi, Quận 1, TP.HCM', '02839257417', '2023-10-01 12:00:00');

--Insert vào bảng audit
INSERT INTO auditorium (name, createdat, format, cinemaid)
VALUES 
('Phòng 1', NOW(), 0, 1),
('Phòng 2', NOW(), 1, 1),
('Phòng 1', NOW(), 0, 2),
('Phòng 2', NOW(), 2, 2),
('Phòng 1', NOW(), 2, 3),
('Phòng 2', NOW(), 1, 3);

SELECT * FROM auditorium
--Insert vào showtime
INSERT INTO showtime (movieid, auditid, starttime, endtime, language)
VALUES
(1, 1, '2025-10-09 09:00:00', '2025-10-06 11:00:00', 'Tiếng Việt'),
(2, 1, '2025-10-09 12:00:00', '2025-10-06 14:30:00', 'English'),
(3, 2, '2025-10-09 15:00:00', '2025-10-06 17:00:00', 'Tiếng Việt'),
(1, 3, '2025-10-10 18:30:00', '2025-10-07 20:45:00', 'English Sub'),
(4, 2, '2025-10-10 21:00:00', '2025-10-07 23:30:00', 'Tiếng Việt'),
(4, 4, '2025-10-10 21:00:00', '2025-10-07 23:30:00', 'Tiếng Việt');


INSERT INTO Seat (rowlabel, active, seatnumber, seattype, auditid)
VALUES 	('A', true, '02', 0, 2),
		('A', true, '03', 0, 2),
		('A', true, '04', 0, 2),
		('B', true, '01', 1, 2),
		('B', true, '02', 1, 2),
		('B', true, '03', 1, 2),
		('B', true, '04', 1, 2),
		('C', true, '01', 1, 2),
		('C', true, '02', 1, 2),
		('C', true, '03', 1, 2),
		('C', true, '04', 1, 2);
		