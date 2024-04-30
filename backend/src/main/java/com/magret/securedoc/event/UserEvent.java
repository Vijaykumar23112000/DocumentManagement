package com.magret.securedoc.event;


import com.magret.securedoc.entity.UserEntity;
import com.magret.securedoc.enumeration.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEvent {
    private UserEntity user;
    private EventType type;
    private Map<? , ?> data;
}
