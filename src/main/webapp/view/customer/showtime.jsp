<%@ include file="/common/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="java.time.LocalDate"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<!DOCTYPE html>
<html>
<head>
    <title>Đặt vé phim</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/showtime.css">
</head>
<body>
<div class="container">
    <div class="row-g3">
        <!-- Cột giữa: Rạp -->
        <div class="col-3">
            <div class="cinema-list">
                <h6 class="fw-bold mb-3">Rạp</h6>
                <ul class="list-group">
                    <c:forEach var="p" items="${partners}">
                        <div class="brand-section">
                            <h2 class="brand-name">${p.brand}</h2>
                            <c:forEach var="cinema" items="${p.cinemas}">
                                <li class="list-group-item">
                                    <form action="showtime" method="post">
                                        <input type="hidden" name="id" value="${cinema.id}">
                                        <input type="hidden" name="action" value="filter">
                                        <input type="hidden" name="selectedDate" value="${selectedDate}">
                                        <button type="submit" class="cinemaBtn ${cinema.id == selectedCinemaId ? 'selected' : ''}">${cinema.name}</button>
                                    </form>
                                </li>
                            </c:forEach>
                        </div>
                    </c:forEach>
                </ul>
            </div>
        </div>

        <!-- Cột phải: Lịch chiếu -->
        <div class="col-7">
            <!-- Thanh chọn ngày -->
            <%
                LocalDate today = LocalDate.now();
                DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/M");
                String[] days = {"CN", "Th 2", "Th 3", "Th 4", "Th 5", "Th 6", "Th 7"};
                LocalDate selectedDate = (LocalDate) request.getAttribute("selectedDate");
                if (selectedDate == null) {
                    selectedDate = today; // fallback nếu chưa chọn
                }
            %>

            <div class="datebtn">
                <%
                    for (int i = 0; i < 7; i++) {
                        LocalDate d = today.plusDays(i);
                        String label = dateFmt.format(d) + "<br>" + days[d.getDayOfWeek().getValue() % 7];
                        boolean isSelected = selectedDate != null && selectedDate.equals(d);
                        String btnClass = isSelected ? "btn btn-primary" : "btn btn-light";
                %>
                <form action="showtime" method="post" style="display:inline;">
                    <input type="hidden" name="selectedDate" value="<%= d.toString() %>">
                    <input type="hidden" name="id" value="<%= request.getAttribute("selectedCinemaId") != null ? request.getAttribute("selectedCinemaId") : "" %>">
                    <input type="hidden" name="action" value="filter">
                    <button type="submit" class="<%= btnClass %>"><%= label %></button>
                </form>
                <%
                    }
                %>
            </div>

            <div class="showtime-card">
                <div class="d-flex">
                    <c:forEach var="entry" items="${movieShowtimes}">
                        <c:set var="movie" value="${entry.key}" />
                        <c:set var="showtimes" value="${entry.value}" />

                        <div class="movie-card">
                            <h2>${movie.title}</h2>
                            <img src="${movie.posterUrl}" alt="${movie.title}" class="movie-poster">
                            <div class="showtime-buttons">
                                <c:forEach var="s" items="${showtimes}">
                                    <c:set var="disableButton" value="${disableMap[s.id]}" />
                                    <form action="booking" method="post" style="display:inline;">
                                        <input type="hidden" name="showtimeID" value="${s.id}">
                                        <input type="hidden" name="action" value="showtimeSl">
                                        <button type="submit" class="time-btn ${disableButton ? 'past' : ''}"
                                            ${disableButton ? 'disabled' : ''}>
                                                ${fn:substring(s.startTime.toString(), 11, 16)}
                                        </button>
                                    </form>
                                </c:forEach>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
