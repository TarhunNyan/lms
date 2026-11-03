package org.sg01.example.lms.helper;

import lombok.RequiredArgsConstructor;
import org.sg01.example.lms.dto.GroupCreateDTO;
import org.sg01.example.lms.dto.GroupDTO;
import org.sg01.example.lms.service.GroupService;

@RequiredArgsConstructor
public class FooGroupTestHelper {

    private final GroupService groupService;
    private GroupDTO groupDTO;

    public GroupDTO getGroupDTO() {
        if (groupDTO != null) {
            return groupDTO;
        }
        GroupCreateDTO groupCreateDTO = new GroupCreateDTO("FOO_GROUP", null);
        return groupService.create(groupCreateDTO);
    }

    public Long getFooGroupId() {
        return getGroupDTO().getGroupId();
    }

}
