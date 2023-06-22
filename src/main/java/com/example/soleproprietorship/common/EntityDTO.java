package com.example.soleproprietorship.common;

import com.example.soleproprietorship.config.TotpService;
import com.example.soleproprietorship.user.User;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class EntityDTO<ENTITY extends HasModel, CREATION_DTO, DTO> {

    @Autowired
    private TotpService totpService;

    protected abstract DTO mapEntityToDTO(ENTITY entity);

    protected abstract ENTITY mapCreationDTOToEntity(CREATION_DTO creationDto);


    public void validate2FA(User user, String code) {

        if (!user.isUsing2FA())
            return;

        try {
            Integer.parseInt(code);
        } catch (NumberFormatException ex) {
            throw new NumberFormatException("Wprowadzony kod nie jest poprawnie sformatowany");
        }

        if (!totpService.verifyCode(user.getSecret2FA(), Integer.parseInt(code)))
            throw new ResponseEntityException("Podałeś niepoprawny kod");

    }

}
