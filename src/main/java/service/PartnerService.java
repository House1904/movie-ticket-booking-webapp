package service;

import dao.PartnerDAO;
import model.Partner;

import java.util.List;

public class PartnerService {
    private PartnerDAO partnerDAO;

    public PartnerService() {
        this.partnerDAO = new PartnerDAO();
    }

    public void addPartner(Partner partner) {
        partnerDAO.addPartner(partner);
    }

    public void updatePartner(Partner partner) {
        partnerDAO.updatePartner(partner);
    }

    public void deletePartner(long partnerId) {
        partnerDAO.deletePartner(partnerId);
    }

    public Partner findPartnerById(long partnerId) {
        return partnerDAO.findPartnerById(partnerId);
    }

    public List<Partner> getAllPartners() {
        return partnerDAO.getAllPartners();
    }
}