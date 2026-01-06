document.addEventListener('DOMContentLoaded', function() {
    const videoPlayer = document.getElementById('videoPlayer');
    const adContainer = document.getElementById('ad-container');
    const adText = document.getElementById('ad-text');
    const closeAdBtn = document.getElementById('close-ad');

    // 视频暂停时触发广告
    videoPlayer.addEventListener('pause', function() {
        loadAndShowAd();
    });

    // 定期检查视频是否暂停，以触发广告
    setInterval(function() {
        if (videoPlayer.paused) {
            loadAndShowAd();
        }
    }, 1000);

    // 关闭广告按钮事件
    closeAdBtn.addEventListener('click', function() {
        adContainer.style.display = 'none';
        // 恢复播放视频
        videoPlayer.play();
    });

    // 加载广告
    function loadAndShowAd() {
        // 调用广告API获取广告内容
        fetch('http://localhost:8080/api/advertisements/random')
            .then(response => response.json())
            .then(data => {
                if (data && data.title) {
                    adText.textContent = data.title + ': ' + data.description;
                    adContainer.style.display = 'block';
                } else {
                    adText.textContent = '暂无广告';
                    adContainer.style.display = 'block';
                }
            })
            .catch(error => {
                console.error('获取广告时出错:', error);
                adText.textContent = '广告加载失败';
                adContainer.style.display = 'block';
            });
    }
});