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

  task demo: :environment do
  	body = {
  		fahrenheit: (85 + 1 * rand)
  	}
  	request = Typhoeus.post("localhost:3000/api/v1/temperatures.json", body: body)
  	# response = JSON.parse(request.response_body)
  end

end
