package service;

import dao.TicketDAO;
import model.Ticket;

import java.util.List;

public class TicketService {
    private TicketDAO ticketDAO = new TicketDAO();
    public List<Ticket> getTicketList(long customerID){
        return ticketDAO.getTicketByCustomer(customerID);
    }
}
