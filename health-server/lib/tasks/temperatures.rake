namespace :temperatures do
  desc "TODO"
  task populate: :environment do
  	print "Running "
  	(0..1000).each do |i|
			fahrenheit = 88 + 5 * rand
  		Temperature.create(fahrenheit: fahrenheit)
  		if (i % 100) == 0
  			print "."
  		end
  	end

  	puts " Done!"
  end

end
