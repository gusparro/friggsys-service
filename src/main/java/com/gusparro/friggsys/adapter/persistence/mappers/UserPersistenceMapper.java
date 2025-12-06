package com.gusparro.friggsys.adapter.persistence.mappers;

import com.gusparro.friggsys.adapter.persistence.entities.UserEntity;
import com.gusparro.friggsys.domain.entities.User;
import com.gusparro.friggsys.domain.vos.Email;
import com.gusparro.friggsys.domain.vos.Name;
import com.gusparro.friggsys.domain.vos.Password;
import com.gusparro.friggsys.domain.vos.Telephone;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getName(),
                user.getTelephone(),
                user.getEmail(),
                user.getPassword(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public User toDomain(UserEntity entity) {
        return User.reconstruct(
                entity.getId(),
                Name.of(entity.getName()),
                Email.of(entity.getEmail()),
                Telephone.of(entity.getTelephone()),
                Password.ofHash(entity.getPasswordHash()),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
