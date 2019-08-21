package com.stylefeng.guns.modular.tag.service.impl;

import com.stylefeng.guns.modular.system.model.Tag;
import com.stylefeng.guns.modular.system.dao.TagMapper;
import com.stylefeng.guns.modular.tag.service.ITagService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-02
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

}
