const songIds = [
  'Z77W088A', 'Z76B7ED9', 'Z7ZBW660', 'Z7Z9BI6W', 'Z76O07CO',
  'Z6WZD78I', 'Z7U00WDE', 'Z677ODE7', 'Z7I6BCCO', 'ZUB790F8',
  'ZZDD86BZ', 'ZZB8UW0E', 'Z76EZE8E', 'Z77OI6IW', 'Z7ZE66UC',
  'Z6FWCOO0', 'Z6EUB99Z', 'Z700DDZW', 'ZZFDIEB6', 'ZZ9Z6UOZ',
  'Z6AIA0I8', 'Z67CFZDA', 'Z76ZD9EO', 'Z7WFEIDA', 'Z7IADBDF',
  'Z6AABFU6', 'Z7IOEUZ0', 'Z7799E7O', 'ZUZA6AF7', 'Z7UUAFUF',
  'Z7ZDBC6B', 'Z7IBDFI7', 'Z7U6A76E', 'Z7ZDAO66', 'Z77UAWDI',
  'ZZDDBWF6', 'ZZ0W0D9W', 'Z6708WAZ', 'Z6600ZCO', 'Z7WEO76A',
  'Z76EAWFO', 'Z7OB97FO', 'Z76UCD09', 'Z7UCU6OW', 'Z7ZUF0OC',
  'ZZDFBFD8', 'Z6Z7AWZU', 'Z77WI7DE', 'ZZEF769O', 'Z7Z8WEF0',
  'Z7Z80CZI', 'Z76IBZW9', 'Z6UUABUW', 'Z7WCEUF9', 'Z7WDD6OF',
  'Z76E0U7Z', 'Z70ACF6A', 'Z76FIWZ0', 'Z6EWUA0Z', 'ZU7UC9ZC',
  'ZUW9EB6F', 'Z6Z0B7I0', 'Z6ED68UB', 'Z77WW8FA', 'ZU7C8FDU',
  'Z6709W0Z', 'Z6Z9A0W0', 'Z6B6ZU0D', 'Z69CEF7I', 'ZUUECEIC',
  'Z69AO98C', 'Z6UD089Z', 'Z6ACUFEI', 'ZUB8CDE9', 'ZZBUE7ZA',
  'Z6ZO8B0O', 'Z7ZWUO0U', 'ZUOZWCUC', 'Z708O0OU', 'Z6EZZWAD',
  'Z7I8899A', 'Z7ODDFD7', 'Z696WZ06', 'Z6I76897', 'Z6DA0D09',
  'Z6797BZD', 'Z7Z9Z7ZC', 'Z6AFC9WD', 'Z70A6II8', 'Z70OZIIB',
  'Z6I7OBZD', 'Z67BD9AC', 'Z7Z69A8U', 'Z76OECEF', 'Z7WIE7U6',
  'Z6ABFCZ6', 'Z767C6ZB', 'Z7UADWCI', 'Z7Z0DCU9'
]

const { ZingMp3 } = require("zingmp3-api-full")

class AppController {
  async getAllSongs(req, res) {
    console.log("Get All Songs")
    // const playlist = await ZingMp3.getDetailPlaylist("ZWZB969E");
    let songs = [];
    let promises = [];

    // playlist.data.song.items.forEach( (song) => {
    songIds.forEach((songId) => {
      promises.push(
        (async () => {
          // const songInfo = await ZingMp3.getInfoSong(song.encodeId);
          // const songData = await ZingMp3.getSong(song.encodeId);
          const songInfo = await ZingMp3.getInfoSong(songId);
          const songData = await ZingMp3.getSong(songId);

          if (songData.err != 0) {
            return;
          }

          songs.push({
            id: songInfo.data.encodeId,
            title: songInfo.data.title,
            artist: {
              id: songInfo.data.artists[0].id,
              name: songInfo.data.artists[0].name,
              thumbnail: songInfo.data.artists[0].thumbnailM
            },
            duration: songInfo.data.duration,
            thumbnail: songInfo.data.thumbnailM,
            path: songData.data[Object.keys(songData.data)[0]]
          });
        })()
      )
    })

    await Promise.all(promises);
    res.json(songs);

    // res.json([
    //     {
    //         id: 1,
    //         title: "Doraemon",
    //         artist: "Nobita",
    //         duration: 120,
    //         thumbnail: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8qVLXZ511VApVjnOcB75QtyyTyO-D_3mDXA&s",
    //         path: "https://codeskulptor-demos.commondatastorage.googleapis.com/pang/paza-moduless.mp3"
    //     }
    // ])
  }
}

module.exports = new AppController