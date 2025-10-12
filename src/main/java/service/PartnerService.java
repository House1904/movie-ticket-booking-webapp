package service;

import dao.PartnerDAO;
import model.Partner;

public class PartnerService {
    private PartnerDAO partnerDAO = new PartnerDAO();
    public boolean addPartner(Partner partner) {
        return partnerDAO.insert(partner);
    }
}