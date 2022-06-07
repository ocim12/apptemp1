package com.example.accr.Dtos;

import java.util.List;

public class AppToReturn {
    public int id;
    public String name;
    public String description;
    public String nameOfUser;
    public String surname;
    public String adminComment;
    public boolean approved;
    public List<UserAttachmentToReturn> userAttachments;
}
