require 'nokogiri'

path = '/home/serg/projects/advprogramming/pool/'
output = '/home/serg/projects/advprogramming/data/'
for dir in Dir.entries(path).select {|entry| File.directory? File.join(path, entry) and !(entry == '.' || entry == '..')}
  # puts File.join(path, dir)
  for item in Dir.glob("#{File.join(path, dir)}/**/*") do
    if item =~ /.*-\d+/ and not File.directory?(item)
      # puts item
      # puts item.split('/')[-1].split('-')[0..-2].join(' ')
      # puts item.split('/')[-1].split('-')[-1]
      doc = File.open(item) { |f| Nokogiri::HTML(f) }
      if !doc.xpath("//h1[@class='story-body__h1']").first.nil? && !doc.xpath("//li[@class='mini-info-list__item']/div/@data-datetime").first.nil? && !doc.xpath("//div[@class='story-body__inner' and @property='articleBody']/p").nil?
        puts File.join(output, dir + "-" + item.split('/')[-1].split('-')[-1])
        File.open(File.join(output, dir + "-" + item.split('/')[-1].split('-')[-1]), 'w') do |file|
          file.write(dir + "%%")
          file.write(item.split('/')[-1].split('-')[0..-2].join(' ') + "%%")
          file.write(item.split('/')[-1].split('-')[-1] + "%%")
          file.write(doc.xpath("//li[@class='mini-info-list__item']/div/@data-datetime").first.text + "%%")
          file.write(doc.xpath("//h1[@class='story-body__h1']").first.text + "%%")
          cnt = doc.xpath("//div[@class='story-body__inner' and @property='articleBody']/p")
          file.write(cnt.inject(""){|sum, x| sum + x.content }) if not cnt.empty?
        end
      end
    end
    File.delete(item) if not File.directory? item
  end
end
