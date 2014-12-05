module Api
	module V1
		class TemperaturesController < ApplicationController

			def index
				render json: { temperatures: Temperature.all.order("id DESC") }
			end

			def show
				temperature = Temperature.find_by_id(params[:id])
				if temperature
					render json: { success: true, temperature: temperature }
				else
					render json: { success: false, message: "No temperature available" }
				end
			end

			def create
				temperature = Temperature.create({
					fahrenheit: params[:fahrenheit]
				})
				if temperature.valid?
					render json: { success: true, temperature: temperature }
				else
					render json: { success: false, message: "Failed to create temperature" }
				end
			end

			def update

			end

			def destroy
				temperature = Temperature.find_by_id(params[:id]).destroy()
				if temperature
					render json: { success: true, message: "Deleted temperature" }
				else
					render json: { success: false, message: "Failed to delete temperature" }
				end
			end

			private

				# def temperature_params
				# 	params.require(:temperature).permit(:fahrenheit, :celsius)
				# end
		end
	end
end