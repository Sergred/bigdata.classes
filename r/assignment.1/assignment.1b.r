setwd('/home/serg/projects/advprogramming/bigdata/r/assignment.1/')

customer <- read.csv("customer.csv", header = TRUE)

# head(customer)
# str(customer)

# "euclidean", "maximum", "manhattan", "canberra", "binary" or "minkowski"

customer <- scale(customer[,-1])

hc <- hclust(dist(scaled, method = "euclidean"), method = "ward.D2")
plot(hc, hang = -0.01, cex = 0.7)

hc2 <- hclust(dist(scaled, method = "euclidean"), method = "single")
plot(hc2, hang = -0.01, cex = 0.7)

fit <- cutree(hc, k = 4)

fit2 <- cutree(hc2, k = 4)

# table(fit)
# fit
# 1  2  3  4
# 11  8 16 25

plot(hc)
rect.hclust(hc, k = 4 , border="red")
# rect.hclust(hc, k = 4 , which = 2, border = "red")

# plot(hc2)
# rect.hclust(hc2, k = 4 , border="red")
# rect.hclust(hc2, k = 4 , which = 2, border = "red")

# set some arbitrarily chosen seed
set.seed(22)
fit <- kmeans(customer, 4)
# fit

plot(customer, col = fit$cluster)

plot(customer[, c("Sex", "Age")], col = fit$cluster)

# gfortran, liblapack-dev required
# install.packages("fpc")
library(fpc)

single_c <- hclust(dist(customer), method="single")
hc_single <- cutree(single_c, k = 4)
complete_c <- hclust(dist(customer), method="complete")
hc_complete <- cutree(complete_c, k = 4)

set.seed(22)
km <- kmeans(customer, 4)

cs <- cluster.stats(dist(customer), km$cluster)

cs[c("within.cluster.ss","avg.silwidth")]

sapply(list(kmeans = km$cluster, hc_single = hc_single,
hc_complete = hc_complete), function(c)
cluster.stats(dist(customer), c)[c("within.cluster.ss","avg.silwidth")])

nk <- 2:10
set.seed(22)

WSS <- sapply(nk, function(k) {kmeans(customer, centers=k)$tot.withinss })
# WSS

# plot it
plot(nk, WSS, type = "l", xlab = "number of k", ylab = "within sum of squares")

# silhouette width
SW <- sapply(nk, function(k) { cluster.stats(dist(customer), kmeans(customer, centers=k)$cluster)$avg.silwidth})
# SW

# plot it
plot(nk, SW, type = "l", xlab = "number of clusters", ylab = "average silhouette width")
