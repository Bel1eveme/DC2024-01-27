package com.bsuir.nastassiayankova.beans.response;

import java.util.List;

public record UserResponseTo(Long id, String login, String password, String firstname, String lastname,
                             List<NewsResponseTo> newsList) {

}
