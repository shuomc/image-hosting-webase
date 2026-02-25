package moe.imtop1.imagehosting.images.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import moe.imtop1.imagehosting.images.entity.Download;
import moe.imtop1.imagehosting.images.mapper.DownloadMapper;
import moe.imtop1.imagehosting.images.service.IDownloadService;
import org.springframework.stereotype.Service;

@Service
public class DownloadServiceImpl extends ServiceImpl<DownloadMapper, Download> implements IDownloadService {
}
