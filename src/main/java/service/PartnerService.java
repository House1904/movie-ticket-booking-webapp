package service;

import dao.PartnerDAO;
import model.Partner;

import java.util.List;

public class PartnerService {
    private PartnerDAO partnerDAO = new PartnerDAO();
    public void addPartner(Partner partner) {
        partnerDAO.insert(partner);
    }
    public void updatePartner(Partner partner) {
        partnerDAO.update(partner);
    }
    public List<Partner> getAllPartners() {
        return partnerDAO.selectAll();
    }
}
