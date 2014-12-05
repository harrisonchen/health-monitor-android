module Api
	module V1
		class HeartbeatsController < ApplicationController

			def index
				render json: { heartbeats: Heartbeat.all.order("id DESC") }
			end

			def show
				heartbeat = Heartbeat.find_by_id(params[:id])
				if heartbeat
					render json: { success: true, heartbeat: heartbeat }
				else
					render json: { success: false, message: "No heartbeat available" }
				end
			end

			def create
				heartbeat = Heartbeat.create({
					beats_per_minute: params[:beats_per_minute]
				})
				if heartbeat.valid?
					render json: { success: true, heartbeat: heartbeat }
				else
					render json: { success: false, message: "Failed to create heartbeat" }
				end
			end

			def update

			end

			def destroy
				heartbeat = Heartbeat.find_by_id(params[:id]).destroy()
				if heartbeat
					render json: { success: true, message: "Deleted heartbeat" }
				else
					render json: { success: false, message: "Failed to delete heartbeat" }
				end
			end

			private

				# def heartbeat_params
				# 	params.require(:heartbeat).permit(:beats_per_second, :beats_per_minute)
				# end
		end
	end
end