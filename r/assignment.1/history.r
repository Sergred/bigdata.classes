y <- 10
f <- function(x) {}
f <- function(x) {
y <- 2
y^2 + g(x)
}
f <- function(x) {
x*y
}
f(3)
f <- function(x) {
y <- 2
y^2 + g(x)
}
g <- function(x) {
x*y
}
f(3)
library(plyr)
library(ggplot2)
library(reshape2)
setwd('/home/serg/projects/advprogramming/bigdata/r/assignment.1/')
vehicles <- read.csv("vehicles.csv", stringsAsFactors = F)
# head(vehicles)
labels <- do.call(rbind, strsplit(readLines("data.passport.txt")," - "))
class(labels) # matrix
labels <- data.frame(labels)
# class(labels) # data.frame
# labels <- read.table("data.passport.txt",	sep	=	"-", header = F)
names(labels) <- c("label", "description")
# head(labels)
nrow(vehicles) # 37936
ncol(vehicles) # 83
names(vehicles)
length(unique(vehicles[,"year"]))	# 34
ncol(vehicles) == ncol(labels) # FALSE
min(vehicles[,"year"]) # 1984
max(vehicles[,"year"]) # 2017
vehicles$trany[vehicles$trany == ""] <- NA
vehicles$trany2 <- ifelse(substr(vehicles$trany, 1, 4) == "Auto", "Auto", "Manual")
# class(vehicles$trany2) # character
vehicles$trany2 <- as.factor(vehicles$trany2)
# class(vehicles$trany2) # factor
table(vehicles$trany2) # {"Auto": 25534, "Manual": 12391}
mpgByYr <- ddply(
vehicles,
~year,
summarise,
avgMPG	=	mean(comb08),
avgHghy	=	mean(highway08),
avgCity	=	mean(city08)
)
ggplot(mpgByYr,	aes(year, avgMPG)) + geom_point() + geom_smooth() + xlab("Year") + ylab("Average MPG") + ggtitle("All cars")
mpgByYrA	<-	ddply(
subset(vehicles, vehicles$trany2 == "Auto"),
~year,
summarise,
avgMPG	=	mean(comb08),
avgHghy	=	mean(highway08),
avgCity	=	mean(city08)
)
mpgByYrM	<-	ddply(
subset(vehicles, vehicles$trany2 != "Auto"),
~year,
summarise,
avgMPG	=	mean(comb08),
avgHghy	=	mean(highway08),
avgCity	=	mean(city08)
)
ggplot()
+ geom_point(data = mpgByYrM, aes(year, avgMPG))
+ geom_smooth(data = mpgByYrM, aes(year, avgMPG, color = "red"))
+ geom_point(data = mpgByYrA, aes(year, avgMPG))
+ geom_smooth(data = mpgByYrA, aes(year, avgMPG, color = "blue"))
+ xlab("Year") + ylab("Average MPG") + ggtitle("All cars")
gasCars <- subset(vehicles, vehicles$fuelType1 %in% c("Regular Gasoline", "Premium Gasoline", "Midgrade Gasoline") & vehicles$fuelType2 == "" & vehicles$atvType != "Hybrid")
mpgByYrG <- ddply(
gasCars,
~year,
summarise,
avgMPG	=	mean(comb08),
avgHghy	=	mean(highway08),
avgCity	=	mean(city08)
)
ggplot()
+ geom_point(data = mpgByYrM, aes(year, avgMPG))
+ geom_smooth(data = mpgByYrM, aes(year, avgMPG, color = "red"))
+ geom_point(data = mpgByYrA, aes(year, avgMPG))
+ geom_smooth(data = mpgByYrA, aes(year, avgMPG, color = "blue"))
+ xlab("Year") + ylab("Average MPG") + ggtitle("All cars")
ggplot() + geom_point(data = mpgByYrM, aes(year, avgMPG)) + geom_smooth(data = mpgByYrM, aes(year, avgMPG, color = "red")) + geom_point(data = mpgByYrA, aes(year, avgMPG)) + geom_smooth(data = mpgByYrA, aes(year, avgMPG, color = "blue")) + xlab("Year") + ylab("Average MPG") + ggtitle("All cars")
summary(gasCars)
gasCars <- subset(gasCars, select = c('barrels08', 'city08', 'co2', 'co2TailpipeGpm', 'comb08', 'cylinders', 'displ', 'drive', 'feScore', 'fuelType1', 'ghgScore', 'highway08', 'UCity', 'UHighway', 'VClass', 'year', 'tCharger', 'sCharger', 'atvType', 'trany2', 'startStop'))
View(gasCars)
View(gasCars)
gasCars$startStop <- as.factor(gasCars$startStop)
summary(gasCars)
startStopY <- subset(gasCars, gasCars$startStop == 'Y')
startStopN <- subset(gasCars, gasCars$startStop == 'N')
mpgByYrY <- ddply(
startStopY,
~year,
summarise,
avgMPG	=	mean(comb08),
avgHghy	=	mean(highway08),
avgCity	=	mean(city08)
)
mpgByYrN <- ddply(
startStopN,
~year,
summarise,
avgMPG	=	mean(comb08),
avgHghy	=	mean(highway08),
avgCity	=	mean(city08)
)
View(mpgByYrY)
View(mpgByYrN)
ggplot() + geom_point(data = mpgByYrY, aes(year, avgMPG, color = "red")) + geom_point(data = mpgByYrN, aes(year, avgMPG, color = "blue")) + xlab("Year") + ylab("Average MPG") + ggtitle("All cars")
ggplot() + geom_point(data = mpgByYrY, aes(year, avgCity, color = "red")) + geom_point(data = mpgByYrN, aes(year, avgCity, color = "blue")) + xlab("Year") + ylab("Average MPG") + ggtitle("All cars")
View(gasCars)
gasCars[gasCars$co2 == -1] <- NA
gasCars$co2[gasCars$co2 == -1] <- NA
View(gasCars)
mpgByYrN <- ddply(
startStopN,
~year,
summarise,
avgMPG	=	mean(comb08),
avgHghy	=	mean(highway08),
avgCity	=	mean(city08),
)
mpgByYrN <- ddply(
startStopN,
~year,
summarise,
avgMPG	=	mean(comb08),
avgHghy	=	mean(highway08),
avgCity	=	mean(city08),
avgCo2 = mean(co2)
)
mpgByYr <- ddply(
gasCars,
~year,
summarise,
avgMPG	=	mean(comb08),
avgHghy	=	mean(highway08),
avgCity	=	mean(city08),
avgCo2 = mean(co2)
)
View(mpgByYr)
plot(mpgByYr$year, mpgByYr$avgCo2)
mpgByYr <- ddply(
gasCars,
~year,
summarise,
avgMPG	=	mean(comb08),
avgHghy	=	mean(highway08),
avgCity	=	mean(city08),
avgCo2 = mean(co2),
avgBar = mean(barrels08)
)
gasCars$feScore[gasCars$feScore == -1] <- NA
View(gasCars)
gasCars$sCharger[gasCars$sCharger == ""] <- NA
View(gasCars)
gasCars$sCharger <- as.factor(gasCars$sCharger)
gasCars$tCharger <- as.factor(gasCars$tCharger)
summary(gasCars)
gasCars$ghgScore[gasCars$ghgScore == -1] <- NA
savehistory("~/projects/advprogramming/bigdata/r/assignment.1/history.r")
