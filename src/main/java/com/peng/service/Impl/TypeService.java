package com.peng.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.peng.dao.TypeDao;
import com.peng.domain.Type;
import com.peng.service.ITypeService;
import com.peng.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("ITypeService")
public class TypeService implements ITypeService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TypeDao typeDao;

    @Override
    public Type findByid(Integer ty_id) {
        return typeDao.findTypeByid(ty_id);
    }

    @Override
    public List<Type> findall() {
        return typeDao.findallType();
    }

    //缓存
    @Override
    public List<Type> findallPro() {
        String key="TypefindallPro";
        if(redisUtil.hasKey(key)){
            return (List<Type>)redisUtil.get(key);
        }

        List<Type> types = typeDao.findallTypePro();
        redisUtil.set(key,types,60*60);
        return types;
    }


    @Override
    public boolean addORedit(Type type) {
        Integer ty_id = type.getTy_id();
        if (ty_id != null) {//更新
            typeDao.updateType(type);
        } else {//添加
            typeDao.addType(type);
        }
        return true;
    }

    @Override
    public boolean deleteByid(Integer ty_id) {
        Type type = typeDao.findTypeByidPro(ty_id);
        if (type.getBlogs() == null || type.getBlogs().size() == 0) {
            typeDao.deleteTypebyid(ty_id);
            return true;
        } else {
            return false;
        }

    }
}
