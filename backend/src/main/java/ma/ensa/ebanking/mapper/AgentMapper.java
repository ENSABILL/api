package ma.ensa.ebanking.mapper;

import ma.ensa.ebanking.dto.AgentDto;
import ma.ensa.ebanking.models.user.Agent;

public class AgentMapper {
    static public AgentDto toDto(Agent agent){
        return AgentDto.builder()
                .id(agent.getId())
                .email(agent.getEmail())
                .phoneNumber(agent.getPhoneNumber())
                .agencyImm(agent.getAgency().getImm())
                .fullName(agent.getFullName())
                .build();
    }
}
