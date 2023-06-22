package com.example.soleproprietorship.common;

import com.example.soleproprietorship.config.MessageResponse;
import com.example.soleproprietorship.config.TotpService;
import com.example.soleproprietorship.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public abstract class EntityDTO<ENTITY extends HasModel, CREATION_DTO, DTO> {

    @Autowired
    private TotpService totpService;

    protected abstract DTO mapEntityToDTO(ENTITY entity);

    protected abstract ENTITY mapCreationDTOToEntity(CREATION_DTO creationDto);


    public ResponseEntity<?> validate2FA(User user, String code){

        if(!user.isUsing2FA())
            return null;

        try {
            Integer.parseInt(code);
        } catch (NumberFormatException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Wprowadzony kod nie jest poprawnie sformatowany"));
        }

        if(!totpService.verifyCode(user.getSecret2FA(), Integer.parseInt(code)))
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Podałeś niepoprawny kod"));

        return null;
    }

}
