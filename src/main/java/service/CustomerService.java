package service;

import dao.CustomerDAO;
import model.Customer;

public class CustomerService {
    private CustomerDAO customerDAO = new CustomerDAO();
    public boolean updateCustomer(Customer customer){
        return customerDAO.update(customer);
    }
}
