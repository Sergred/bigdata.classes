import os
from bs4 import BeautifulSoup
# from lxml import etree
# page = etree.HTML(data)
# date = page.xpath("//li[@class="mini-info-list__item"]/div/@data-datetime").first().text()
# title = page.xpath("//h1[@class="story-body__h1"]").first().text()
# body = page.xpath("//div[@class="story-body__inner" and @property="articleBody"]/p").join("")

path = "/home/serg/projects/advprogramming/pool/www.bbc.com/news/"

for root, dirs, files in os.walk(path):
    for line in files:
        if line.split('-')[-1].isdigit():
            # print os.path.join(root, line)
            date, body, title = "", "", ""
            soup = BeautifulSoup(open(os.path.join(root, line)), "lxml")
            tmp = soup.find_all("li", class_ = "mini-info-list__item")
            if (tmp is not None) and (len(tmp) != 0):
                date = tmp[0].find(attrs = {"div": "", "data-datetime": True})['data-datetime']
            tmp = soup.find_all("div", class_ = "story-body__inner", property = "articleBody")
            if (tmp is not None) and (len(tmp) != 0):
                body = "".join(["".join([i.string for i in each.find_all("p") if i.string is not None]) for each in tmp if each.p.string is not None])
            tmp = soup.find("h1", class_ = "story-body__h1")
            if tmp is not None:
                title = tmp.string
            print (date, title, body)
