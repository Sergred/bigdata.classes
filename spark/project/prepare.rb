require 'nokogiri'

for item in Dir['/home/serg/projects/advprogramming/bigdata/spark/project/www.bbc.com/news/*'] do
  if item =~ /.*-\d+/ and not File.directory?(item)
    doc = File.open(item) { |f| Nokogiri::HTML(f) }
    # File.open(item, 'w') do |file|
    puts doc.xpath("//h1[@class='story-body__h1']").first
    puts doc.xpath("//li[@class='mini-info-list__item']/div/@data-datetime")
    puts doc.xpath("//div[@class='story-body__inner' and @property='articleBody']/p")
    puts
      # file.write(doc.xpath("//h1[@class='story-body__h1']").first + "\n")
      # file.write(doc.xpath("//li[@class='mini-info-list__item']/div/@data-datetime").first + "\n")
      # cnt = doc.xpath("//div[@class='story-body__inner' and @property='articleBody']/p")
      # file.write(cnt.inject(""){|sum, x| sum + x.content }) if not cnt.empty?
    # end
  else
    # File.delete(item) if not File.directory?(item)
  end
end
