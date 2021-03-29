// create Agora client
var client = AgoraRTC.createClient({mode: "rtc", codec: "vp8"});

var localTracks = {
    videoTrack: null,
    audioTrack: null
};
var remoteUsers = {};
// Agora client options
var options = {
    appid: null,
    channel: null,
    uid: null,
    token: null
};


// 页面一加载就视频通话
$(() => {
    $(document).ready(function () {
        $.ajax({
            type: 'post'
            , url: "http://localhost:4567/token"
            , cache: false
            , processData: false
            , contentType: false
        }).done(async function (res) {
            options.appid = res.appid;
            options.channel = res.channel;
            options.token = res.token;
            try {
                await join();
                $("#success-alert-with-token").css("display", "block");
            } catch (error) {
                console.log(error);
            }
        }).fail(function (res) {
            console.log(res);
        });
    });
});

async function join() {
    // add event listener to play remote tracks when remote user publishs.
    client.on("user-published", handleUserPublished);
    client.on("user-unpublished", handleUserUnpublished);

    // join a channel and create local tracks, we can use Promise.all to run them concurrently
    [options.uid, localTracks.audioTrack, localTracks.videoTrack] = await Promise.all([
        // join the channel
        client.join(options.appid, options.channel, options.token || null),
        // create local tracks, using microphone and camera
        AgoraRTC.createMicrophoneAudioTrack(),
        AgoraRTC.createCameraVideoTrack()
    ]);

    // play local video track
    localTracks.videoTrack.play("local-player");
    $("#local-player-name").text(`localVideo(${options.uid})`);

    // publish local tracks to channel
    await client.publish(Object.values(localTracks));
    console.log("publish success");
}

//这里会显示加入用户的姓名，就是根据uid实现的
async function subscribe(user, mediaType) {
    const uid = user.uid;
    // subscribe to a remote user
    await client.subscribe(user, mediaType);
    console.log("subscribe success");
    if (mediaType === 'video') {
        const player = $(`
      <div id="player-wrapper-${uid}">
        <p class="player-name">remoteUser(${uid})</p>
        <div id="player-${uid}" class="player"></div>
      </div>
    `);
        $("#remote-playerlist").append(player);
        user.videoTrack.play(`player-${uid}`);
    }
    if (mediaType === 'audio') {
        user.audioTrack.play();
    }
}

function handleUserPublished(user, mediaType) {
    const id = user.uid;
    remoteUsers[id] = user;
    subscribe(user, mediaType);
}

function handleUserUnpublished(user) {
    const id = user.uid;
    delete remoteUsers[id];
    $(`#player-wrapper-${id}`).remove();
}