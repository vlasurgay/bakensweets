package com.edu.netc.bakensweets.service;

import com.edu.netc.bakensweets.dto.*;
import com.edu.netc.bakensweets.exception.BadRequestParamException;
import com.edu.netc.bakensweets.exception.CustomException;
import com.edu.netc.bakensweets.mapperConfig.AccountMapper;
import com.edu.netc.bakensweets.mapperConfig.CredentialsMapper;
import com.edu.netc.bakensweets.mapperConfig.KitchenwareMapper;
import com.edu.netc.bakensweets.model.*;
import com.edu.netc.bakensweets.repository.interfaces.AccountRepository;
import com.edu.netc.bakensweets.repository.interfaces.CredentialsRepository;
import com.edu.netc.bakensweets.repository.interfaces.FriendshipRepository;
import com.edu.netc.bakensweets.repository.interfaces.KitchenwareRepository;
import com.edu.netc.bakensweets.security.JwtTokenProvider;
import com.edu.netc.bakensweets.service.interfaces.CaptchaService;
import com.edu.netc.bakensweets.service.interfaces.FriendshipService;

import com.edu.netc.bakensweets.service.interfaces.KitchenwareService;
import com.edu.netc.bakensweets.service.interfaces.WrongAttemptLoginService;
import com.edu.netc.bakensweets.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Service
public class KitchenwareServiceImpl implements KitchenwareService {
    private final KitchenwareRepository kitchenwareRepository;
    private final KitchenwareMapper kitchenwareMapper;

    @Override
    @Transactional
    public Collection<String> getAllCategories() {
        return kitchenwareRepository.getAllCategories();
    }



    @Override
    @Transactional
    public KitchenwareDTO getKitchenwareById(Long id) {
        try {
            return kitchenwareMapper.kitchenwaretoKitchenwareDTO(kitchenwareRepository.findById(id));
        } catch (EmptyResultDataAccessException ex) {
            throw new CustomException(HttpStatus.NOT_FOUND, String.format("Kitchenware with id %s not found.", id));
        }
    }

    @Override
    @Transactional
    public KitchenwareDTO createKitchenware(KitchenwareDTO kitchenwareDTO) {
        try {
            Kitchenware kitchenware = kitchenwareMapper.kitchenwareDTOtoKitchenware(kitchenwareDTO);
            Long id = Utils.generateUniqueId();
            kitchenware.setId(id);
            kitchenwareRepository.create(kitchenware);
            kitchenwareDTO.setId(id.toString());
            kitchenwareDTO.setActive(true);
            return kitchenwareDTO;

        } catch (
        DataIntegrityViolationException ex) {
            throw new BadRequestParamException("category", "Category is invalid");
        }
    }

    @Override
    @Transactional
    public KitchenwareDTO updateKitchenware(KitchenwareDTO kitchenwareDTO, long id) {
        try {
            kitchenwareDTO.setId(String.valueOf(id));
            Kitchenware kitchenware = kitchenwareMapper.kitchenwareDTOtoKitchenware(kitchenwareDTO);
            boolean updated = kitchenwareRepository.update(kitchenware);
            if (!updated) {
                throw new CustomException(HttpStatus.NOT_FOUND, String.format("Kitchenware with id %s not found.", id));
            }
            return kitchenwareDTO;
        } catch (
                DataIntegrityViolationException ex) {
            throw new BadRequestParamException("category", "Category is invalid");
        }
    }

    @Override
    @Transactional
    public void changeKitchenwareStatus(long id) {
        boolean updated = kitchenwareRepository.changeStatusById(id);
        if (!updated) {
            throw new CustomException(HttpStatus.NOT_FOUND, String.format("Kitchenware with id %s not found.", id));
        }
    }

    @Override
    @Transactional
    public ItemsPerPageDTO<KitchenwareDTO> getFilteredKitchenware(String name, List<Object> args, Boolean active, int limit, boolean order, int currentPage) {
        int count = kitchenwareRepository.countFilteredKitchenware(name, args, active);
        Collection<Kitchenware> kitchenwarePage = kitchenwareRepository.filterKitchenware(
                name, args, active, limit,  currentPage * limit, order
        );
        return new ItemsPerPageDTO<>(
                kitchenwareMapper.kitchenwarePageToDtoCollection(kitchenwarePage), currentPage, count
        );
    }
}
