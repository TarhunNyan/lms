package org.sg01.example.lms.helper;

import lombok.RequiredArgsConstructor;
import org.sg01.example.lms.dto.GroupCreateDTO;
import org.sg01.example.lms.dto.GroupDTO;
import org.sg01.example.lms.service.GroupService;

@RequiredArgsConstructor
public class FooGroupTestHelper {

    static String FOO_GROUP_NAME = "FOO_GROUP";
    private final GroupService groupService;
    private GroupDTO groupDTO;

    public GroupDTO getGroupDTO() {
        if (groupDTO == null) {
            GroupCreateDTO groupCreateDTO = new GroupCreateDTO(FooGroupTestHelper.FOO_GROUP_NAME, null);
            groupDTO = groupService.create(groupCreateDTO);
        }
        return groupDTO;
    }

    public Long getFooGroupId() {
        return getGroupDTO().getGroupId();
    }

    public void reset() {
        groupDTO = null;
    }

    public boolean isFooGroup(GroupDTO value) {
        return groupDTO.getGroupId().equals(value.getGroupId());
    }

}
