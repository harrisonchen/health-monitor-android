class Temperature < ActiveRecord::Base
	validates :fahrenheit, presence: true

	before_save :convertions

	def convertions
  	self.celsius = (fahrenheit - 32) * (5.0/9.0)
  	self.kelvin = celsius + 273
  end
end
