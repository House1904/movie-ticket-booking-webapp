package service;

import dao.PartnerDAO;
import model.Partner;

import java.util.List;

public class PartnerService {
    private PartnerDAO partnerDAO = new PartnerDAO();
    public boolean addPartner(Partner partner) {
        return partnerDAO.insert(partner);
    }
    public List<Partner> getAllPartners() {
        return partnerDAO.selectAll();
    }
}
