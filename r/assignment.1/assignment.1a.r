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

unique(vehicles[, "fuelType1"])

ncol(vehicles) == ncol(labels) # FALSE

min(vehicles[,"year"]) # 1984
max(vehicles[,"year"]) # 2017

vehicles$trany[vehicles$trany == ""] <- NA
vehicles$feScore[vehicles$feScore == -1] <- NA

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
  avgCity	=	mean(city08),
  avgCo2  = mean(co2TailpipeGpm),
  avgBar  = mean(barrels08)
)

ggplot(mpgByYr,	aes(year, avgMPG)) + geom_point() + geom_smooth() + xlab("Year") + ylab("Average MPG") + ggtitle("All cars")

mpgByYrA	<-	ddply(
    subset(vehicles, vehicles$trany2 == "Auto"),
    ~year,
    summarise,
    avgMPG	=	mean(comb08),
    avgHghy	=	mean(highway08),
    avgCity	=	mean(city08),
    avgCo2 = mean(co2TailpipeGpm),
    avgScore = mean(feScore),
    avgBar  = mean(barrels08)
)

mpgByYrM	<-	ddply(
    subset(vehicles, vehicles$trany2 != "Auto"),
    ~year,
    summarise,
    avgMPG	=	mean(comb08),
    avgHghy	=	mean(highway08),
    avgCity	=	mean(city08),
    avgCo2 = mean(co2TailpipeGpm),
    avgScore = mean(feScore),
    avgBar  = mean(barrels08)
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
  avgCity	=	mean(city08),
  avgCo2 = mean(co2TailpipeGpm),
  avgScore = mean(feScore),
  avgBar  = mean(barrels08)
)

# summary(gasCars)
gasCars <- subset(gasCars, select = c('barrels08', 'city08', 'co2TailpipeGpm', 'comb08', 'cylinders', 'displ', 'drive', 'feScore', 'fuelType1', 'ghgScore', 'highway08', 'UCity', 'UHighway', 'VClass', 'year', 'tCharger', 'sCharger', 'atvType', 'trany2', 'startStop'))

dieselCars <- subset(vehicles, vehicles$fuelType1 == "Diesel" & vehicles$fuelType2 == "" & vehicles$atvType != "Hybrid")

mpgByYrD <- ddply(
  dieselCars,
  ~year,
  summarise,
  avgMPG	=	mean(comb08),
  avgHghy	=	mean(highway08),
  avgCity	=	mean(city08),
  avgCo2 = mean(co2TailpipeGpm),
  avgScore = mean(feScore),
  avgBar  = mean(barrels08)
)

electricCars <- subset(vehicles, vehicles$fuelType1 == "Electricity" & vehicles$fuelType2 == "" & vehicles$atvType != "Hybrid")

mpgByYrE <- ddply(
  electricCars,
  ~year,
  summarise,
  avgMPG	=	mean(comb08),
  avgHghy	=	mean(highway08),
  avgCity	=	mean(city08),
  avgCo2 = mean(co2TailpipeGpm),
  avgScore = mean(feScore),
  avgBar  = mean(barrels08)
)

mGasCars <- subset(vehicles, vehicles$fuelType1 == "Midgrade Gasoline" & vehicles$fuelType2 == "" & vehicles$atvType != "Hybrid")

mpgByYrM <- ddply(
  mGasCars,
  ~year,
  summarise,
  avgMPG	=	mean(comb08),
  avgHghy	=	mean(highway08),
  avgCity	=	mean(city08),
  avgCo2 = mean(co2TailpipeGpm),
  avgScore = mean(feScore),
  avgBar  = mean(barrels08)
)

ggplot() + geom_point(data = mpgByYrG, aes(year, avgMPG)) + geom_smooth(data = mpgByYrG, aes(year, avgMPG, color = "red")) + geom_point(data = mpgByYrD, aes(year, avgMPG)) + geom_smooth(data = mpgByYrD, aes(year, avgMPG, color = "blue")) + geom_point(data = mpgByYrE, aes(year, avgMPG)) + geom_smooth(data = mpgByYrE, aes(year, avgMPG, color = "green")) + xlab("Year") + ylab("Average MPG") + ggtitle("All cars")

rGasCars <- subset(vehicles, vehicles$fuelType1 == "Regular Gasoline" & vehicles$fuelType2 == "" & vehicles$atvType != "Hybrid")

mpgByYrR <- ddply(
  rGasCars,
  ~year,
  summarise,
  avgMPG	=	mean(comb08),
  avgHghy	=	mean(highway08),
  avgCity	=	mean(city08),
  avgCo2 = mean(co2TailpipeGpm),
  avgScore = mean(feScore),
  avgBar  = mean(barrels08)
)

pGasCars <- subset(vehicles, vehicles$fuelType1 == "Premium Gasoline" & vehicles$fuelType2 == "" & vehicles$atvType != "Hybrid")

mpgByYrP <- ddply(
  pGasCars,
  ~year,
  summarise,
  avgMPG	=	mean(comb08),
  avgHghy	=	mean(highway08),
  avgCity	=	mean(city08),
  avgCo2 = mean(co2TailpipeGpm),
  avgScore = mean(feScore),
  avgBar  = mean(barrels08)
)

ggplot() + geom_point(data = mpgByYrR, aes(year, avgMPG)) + geom_smooth(data = mpgByYrR, aes(year, avgMPG, color = "red")) + geom_point(data = mpgByYrM, aes(year, avgMPG)) + geom_smooth(data = mpgByYrM, aes(year, avgMPG, color = "blue")) + geom_point(data = mpgByYrP, aes(year, avgMPG)) + geom_smooth(data = mpgByYrP, aes(year, avgMPG, color = "green")) + xlab("Year") + ylab("Average MPG") + ggtitle("All cars")
