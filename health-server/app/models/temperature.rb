class Temperature < ActiveRecord::Base
	validates :fahrenheit, presence: true
	validates :celsius, presence: true
end
