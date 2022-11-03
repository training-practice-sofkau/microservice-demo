package org.example.domain.value;

import org.example.generic.domain.Identity;

public class UserId extends Identity {

    public UserId(){

    }
    private UserId(String id) {
        super(id);
    }

    public static UserId of(String id){
        return new UserId(id);
    }
}
