package service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import entity.Demo;
import mapper.DemoMapper;
import org.springframework.stereotype.Service;
import service.DemoService;

/**
 * @author letere
 * @since 2023-08-04
 */
@Service
public class DemoServiceImpl extends ServiceImpl<DemoMapper, Demo> implements DemoService {

}
