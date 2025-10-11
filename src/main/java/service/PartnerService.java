package service;

import dao.PartnerDAO;
import model.Partner;

public class PartnerService {
    private PartnerDAO partnerDAO = new PartnerDAO();
    public void addPartner(Partner partner) {
        partnerDAO.insert(partner);
    }
    public void updatePartner(Partner partner) {
        partnerDAO.update(partner);
    }
}
