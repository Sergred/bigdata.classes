import sys

from pyspark import SparkContext, SparkConf
from pyspark.streaming import StreamingContext

def parse(data):
    from bs4 import BeautifulSoup
    date, body, title = "", "", ""
    soup = BeautifulSoup(data, "lxml")
    tmp = soup.find_all("li", class_ = "mini-info-list__item")
    if (tmp is not None) and (len(tmp) != 0):
        date = tmp[0].find(attrs = {"div": "", "data-datetime": True})['data-datetime']
    tmp = soup.find_all("div", class_ = "story-body__inner", property = "articleBody")
    if (tmp is not None) and (len(tmp) != 0):
        body = "".join(["".join([i.string for i in each.find_all("p") if i.string is not None]) for each in tmp if each.p.string is not None])
    tmp = soup.find("h1", class_ = "story-body__h1")
    if tmp is not None:
        title = tmp.string
    return (date, title, body)

# def parse(data):
#     return tuple(data.split("%%"))


def main():
    conf = (SparkConf()
         .setMaster("local[*]")
         .setAppName("Article Streamer"))
    sc = SparkContext(conf = conf)
    ssc = StreamingContext(sc, 3)
    ssc.sparkContext.setLogLevel("WARN")
    ssc.checkpoint("checkpoint")

    dstream = ssc.textFileStream("/home/serg/projects/advprogramming/pool/www.bbc.com/news/")
    # dstream = ssc.textFileStream("/home/serg/projects/advprogramming/data/")
    # dstream.reduceByWindow(lambda x, y: x + y, None, 3, 3).flatMap(lambda x: x.split("<!DOCTYPE html>")).countByWindow(3, 3).pprint()
    # dstream.reduceByWindow(lambda x, y: x + y, None, 1, 1).flatMap(lambda x: x.split("<!DOCTYPE html>")).pprint()
    cleared = dstream.reduceByWindow(lambda x, y: x + y, None, 3, 3).flatMap(lambda x: x.split("<!DOCTYPE html>")).map(parse).filter(lambda x: x is not None).filter(lambda x: (x[0] != "") and (x[1] != "") and (x[2] != ""))
    cleared.countByWindow(3, 3).pprint()

    ssc.start()
    ssc.awaitTermination()


if __name__ == "__main__":
    main()
