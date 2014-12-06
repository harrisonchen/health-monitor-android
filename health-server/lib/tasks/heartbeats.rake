namespace :heartbeats do
  desc "TODO"
  task demo: :environment do
  	body = {
  		beats_per_minute: (600 + 40 * rand)
  	}
  	request = Typhoeus.post("localhost:3000/api/v1/heartbeats.json", body: body)
  	# response = JSON.parse(request.response_body)
  end

end
