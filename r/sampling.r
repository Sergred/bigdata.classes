# select variables v1, v2, v3
myvars <- c("v1", "v2", "v3")
newdata <- mydata[myvars]

# another method
myvars <- paste("v", 1:3, sep="")
newdata <- mydata[myvars]

# select 1st and 5th thru 10th variables
newdata <- mydata[c(1,5:10)]

# exclude variables v1, v2, v3
myvars <- names(mydata) %in% c("v1", "v2", "v3")
newdata <- mydata[!myvars]

# exclude 3rd and 5th variable
newdata <- mydata[c(-3,-5)]

# delete variables v3 and v5
mydata$v3 <- mydata$v5 <- NULL

# first 5 observations
newdata <- mydata[1:5,]

# based on variable values
newdata <- mydata[ which(mydata$gender == 'F' & mydata$age > 65), ]

# or
attach(newdata)
newdata <- mydata[ which(gender == 'F' & age > 65),]
detach(newdata)

# using subset function
newdata <- subset(mydata, age >= 20 | age < 10, select = c(ID, Weight))

# using subset function (part 2)
newdata <- subset(mydata, sex == "m" & age > 25, select = weight:income)

# take a random sample of size 50 from a dataset mydata
# sample without replacement
mysample <- mydata[sample(1:nrow(mydata), 50, replace = F),]
