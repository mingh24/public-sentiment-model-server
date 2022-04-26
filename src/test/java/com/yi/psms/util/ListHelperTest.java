package com.yi.psms.util;

import com.yi.psms.model.vo.questionnaire.FriendItemVO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListHelperTest {

    @Test
    void extractDuplicateElements() {
        var f1 = new FriendItemVO();
        f1.setStudentId(20210001);
        f1.setIntimacy(1);
        var f2 = new FriendItemVO();
        f2.setStudentId(20210002);
        f2.setIntimacy(2);
        var f3 = new FriendItemVO();
        f3.setStudentId(20210001);
        f3.setIntimacy(3);
        var f4 = new FriendItemVO();
        f4.setStudentId(20210003);
        f4.setIntimacy(3);

        List<FriendItemVO> friendItemList = new ArrayList<>(List.of());
        var duplicateFriendItemList = ListHelper.extractDuplicateElements(friendItemList, FriendItemVO::getStudentId);
        assertArrayEquals(new FriendItemVO[]{}, duplicateFriendItemList.toArray());

        friendItemList = new ArrayList<>(List.of(f1, f2));
        duplicateFriendItemList = ListHelper.extractDuplicateElements(friendItemList, FriendItemVO::getStudentId);
        assertArrayEquals(new FriendItemVO[]{}, duplicateFriendItemList.toArray());

        friendItemList = new ArrayList<>(List.of(f1, f2, f3, f4));
        duplicateFriendItemList = ListHelper.extractDuplicateElements(friendItemList, FriendItemVO::getStudentId);
        assertArrayEquals(new FriendItemVO[]{f1, f3}, duplicateFriendItemList.toArray());

        friendItemList = new ArrayList<>(List.of(f1, f2, f3, f4));
        duplicateFriendItemList = ListHelper.extractDuplicateElements(friendItemList, FriendItemVO::getIntimacy);
        assertArrayEquals(new FriendItemVO[]{f3, f4}, duplicateFriendItemList.toArray());
    }

}