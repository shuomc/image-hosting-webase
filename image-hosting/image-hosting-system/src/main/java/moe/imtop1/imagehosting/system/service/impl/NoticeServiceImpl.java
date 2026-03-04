package moe.imtop1.imagehosting.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import moe.imtop1.imagehosting.system.entity.Notice;
import moe.imtop1.imagehosting.system.mapper.NoticeMapper;
import moe.imtop1.imagehosting.system.service.INoticeService;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {
}
