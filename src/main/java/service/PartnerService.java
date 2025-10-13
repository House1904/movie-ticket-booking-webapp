package service;

import dao.PartnerDAO;
import dao.AccountDAO;
import model.Partner;
import model.Account;
import model.enums.Role;
import org.mindrot.jbcrypt.BCrypt;
import util.DBConnection;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class PartnerService {
    private PartnerDAO partnerDAO = new PartnerDAO();


    public void addPartner(Partner partner) {
        partnerDAO.insert(partner);
        // Tạo tài khoản tự động
    }

    public void updatePartner(Partner partner) {
        partnerDAO.update(partner);
    }

    public void deletePartner(long partnerId) {
        partnerDAO.delete(partnerId);
    }

    public Partner findPartnerById(long partnerId) {
        return partnerDAO.findById(partnerId);
    }

    public List<Partner> getAllPartners() {
        return partnerDAO.findAll();
    }

    // Phương thức để tìm tài khoản dựa trên partnerId
    public boolean isEmailExists(String email) {
        List<Partner> partners = getAllPartners();
        for (Partner p : partners) {
            if (p.getEmail() != null && p.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }


    public boolean isPhoneExists(String phone) {
        List<Partner> partners = getAllPartners();
        for (Partner p : partners) {
            if (p.getPhone() != null && p.getPhone().equals(phone)) {
                return true;
            }
        }
        return false;
    }


}