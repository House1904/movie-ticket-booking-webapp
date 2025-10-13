package service;

import dao.TicketDAO;
import model.Ticket;

import java.util.List;

public class TicketService {
    private TicketDAO ticketDAO = new TicketDAO();

    public List<Ticket> getTicketsByCustomer(long customerId){
        return ticketDAO.getTicketsByCustomerId(customerId);
    }

    public Ticket getTicket(long id){
        return ticketDAO.findById(id);
    }

    public boolean updateTicket(Ticket t){
        return ticketDAO.update(t);
    }
    public boolean insert(Ticket ticket) {
        return ticketDAO.insert(ticket);
    }
    public List<Ticket> getTicketList(long customerID){
        return ticketDAO.getTicketByCustomer(customerID);
    }
    public List<Ticket> getTicketByCustomerAndMovie(long customerId, long movieId) {
        return ticketDAO.getTicketByCustomerAndMovie(customerId, movieId);
    }
}
