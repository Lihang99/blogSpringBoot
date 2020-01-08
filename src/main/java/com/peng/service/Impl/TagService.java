package com.peng.service.Impl;


import com.peng.dao.TagDao;
import com.peng.domain.Tag;
import com.peng.service.ITagService;
import com.peng.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ITagService")
public class TagService implements ITagService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TagDao tagDao;

    @Override
    public Tag findByid(Integer ta_id) {
        return tagDao.findTagByid(ta_id);

    }


    @Override
    public List<Tag> findall() {
        List<Tag> tagList = tagDao.findallTag();
        return tagList;
    }

    //缓存
    @Override
    public List<Tag> findallPro() {
        String key="TagfindallPro";
        if(redisUtil.hasKey(key)){
            return (List<Tag>)redisUtil.get(key);
        }
        List<Tag> tagList = tagDao.findallTagPro();
        redisUtil.set(key,tagList,60*60);
        return tagList;
    }

    @Override
    public boolean addORedit(Tag tag) {
        Integer ta_id = tag.getTa_id();
        if (ta_id != null) {//更新
            tagDao.updateTag(tag);
        } else {//添加
            tagDao.addTag(tag);
        }
        return true;
    }

    @Override
    public boolean deleteByid(Integer ta_id) {
        Tag tag = tagDao.findTagByidPro(ta_id);
        if (tag.getBlogs() == null || tag.getBlogs().size() == 0) {
            tagDao.deleteTagbyid(ta_id);
            return true;
        } else {
            return false;
        }

    }
}
