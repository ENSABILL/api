package ma.ensa.ebanking.mapper;

import ma.ensa.ebanking.dto.ClientDto;
import ma.ensa.ebanking.models.user.Client;

public class ClientMapper {
    static public ClientDto toDto(Client client){
        return ClientDto.builder()
                .id(client.getId())
                .account(client.getAccount())
                .cin(client.getCin())
                .email(client.getEmail())
                .fullName(client.getFullName())
                .phoneNumber(client.getPhoneNumber())
                .username(client.getUsername())
                .build();
    }
}
