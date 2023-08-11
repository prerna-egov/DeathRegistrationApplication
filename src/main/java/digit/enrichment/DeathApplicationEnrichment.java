package digit.enrichment;

import digit.service.DeathRegistrationService;
import digit.service.UserService;
import digit.utils.IdgenUtil;
import digit.utils.UserUtil;
import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
@Component
@Slf4j
public class DeathApplicationEnrichment {

    @Autowired
    private IdgenUtil idgenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private UserUtil userUtils;

    public void enrichDeathApplication(DeathRegistrationRequest DeathRegistrationRequest) {
        List<String> DeathRegistrationIdList = idgenUtil.getIdList(DeathRegistrationRequest.getRequestInfo(), DeathRegistrationRequest.getDeathRegistrationApplications().get(0).getTenantId(), "dtr.registrationid", "", DeathRegistrationRequest.getDeathRegistrationApplications().size());
        Integer index = 0;
        for(DeathRegistrationApplication application : DeathRegistrationRequest.getDeathRegistrationApplications()){
            // Enrich audit details
            AuditDetails auditDetails = AuditDetails.builder().createdBy(DeathRegistrationRequest.getRequestInfo().getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(DeathRegistrationRequest.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();
            application.setAuditDetails(auditDetails);

            // Enrich UUID
            application.setId(UUID.randomUUID().toString());

//            application.getFather().setId(application.getId());
//            application.getMother().setId(application.getId());

            // Enrich registration Id
            application.getAddressOfDeceased().setRegistrationId(application.getId());

            // Enrich add`ress UUID
//            application.getAddressOfDeceased().setRegistrationId(UUID.randomUUID().toString());

            //Enrich application number from IDgen
            application.setApplicationNumber(DeathRegistrationIdList.get(index++));

        }
    }

    public void enrichDeathApplicationUponUpdate(DeathRegistrationRequest DeathRegistrationRequest) {
        // Enrich lastModifiedTime and lastModifiedBy in case of update
        DeathRegistrationRequest.getDeathRegistrationApplications().get(0).getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
        DeathRegistrationRequest.getDeathRegistrationApplications().get(0).getAuditDetails().setLastModifiedBy(DeathRegistrationRequest.getRequestInfo().getUserInfo().getUuid());
    }

//    public void enrichFatherApplicantOnSearch(DeathRegistrationApplication application) {
//        UserDetailResponse fatherUserResponse = userService.searchUser(userUtils.getStateLevelTenant(application.getTenantId()),application.getFather().getId(),null);
//        User fatherUser = fatherUserResponse.getUser().get(0);
//        log.info(fatherUser.toString());
//        FatherApplicant fatherApplicant = FatherApplicant.builder().aadhaarNumber(fatherUser.getAadhaarNumber())
//                .accountLocked(fatherUser.getAccountLocked())
//                .active(fatherUser.getActive())
//                .altContactNumber(fatherUser.getAltContactNumber())
//                .bloodGroup(fatherUser.getBloodGroup())
//                .correspondenceAddress(fatherUser.getCorrespondenceAddress())
//                .correspondenceCity(fatherUser.getCorrespondenceCity())
//                .correspondencePincode(fatherUser.getCorrespondencePincode())
//                .gender(fatherUser.getGender())
//                .id(fatherUser.getUuid())
//                .name(fatherUser.getName())
//                .type(fatherUser.getType())
//                .roles(fatherUser.getRoles()).build();
//        application.setFather(fatherApplicant);
//    }

//    public void enrichMotherApplicantOnSearch(DeathRegistrationApplication application) {
//        UserDetailResponse motherUserResponse = userService.searchUser(userUtils.getStateLevelTenant(application.getTenantId()),application.getFather().getId(),null);
//        User motherUser = motherUserResponse.getUser().get(0);
//        log.info(motherUser.toString());
//        MotherApplicant motherApplicant = MotherApplicant.builder().aadhaarNumber(motherUser.getAadhaarNumber())
//                .accountLocked(motherUser.getAccountLocked())
//                .active(motherUser.getActive())
//                .altContactNumber(motherUser.getAltContactNumber())
//                .bloodGroup(motherUser.getBloodGroup())
//                .correspondenceAddress(motherUser.getCorrespondenceAddress())
//                .correspondenceCity(motherUser.getCorrespondenceCity())
//                .correspondencePincode(motherUser.getCorrespondencePincode())
//                .gender(motherUser.getGender())
//                .id(motherUser.getUuid())
//                .name(motherUser.getName())
//                .type(motherUser.getType())
//                .roles(motherUser.getRoles()).build();
//        application.setMother(motherApplicant);
//    }
}
